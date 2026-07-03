package com.productivity.backend.task;

import com.productivity.backend.entity.Task;
import com.productivity.backend.entity.User;
import com.productivity.backend.task.dto.TaskRequest;
import com.productivity.backend.task.dto.TaskResponse;
import com.productivity.backend.task.event.TaskCompletedEvent;
import com.productivity.backend.task.exception.TaskNotFoundException;
import com.productivity.backend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public TaskResponse createTask(TaskRequest request) {
        UUID userId  = getCurrentUserId();
        User user = userRepository.getReferenceById(userId);
        Task task = new Task();
        task.setUser(user);
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setPriority(request.priority() != null ? request.priority() : "MEDIUM");
        task.setDueDate(request.dueDate());
        task.setCategory(request.category());
        task.setStatus("TODO");

        taskRepository.save(task);
        return toResponse(task);
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> list(String status) {
        UUID userId = getCurrentUserId();
        List<Task> tasks = status != null
                ? taskRepository.findByUserIdAndStatusOrderByCreatedAtDesc(userId, status)
                : taskRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return tasks.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public TaskResponse getById(UUID id) {
        UUID userId = getCurrentUserId();
        Task task = taskRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new TaskNotFoundException(id));
        return toResponse(task);
    }

    public TaskResponse update(UUID id, TaskRequest request) {
        UUID userId = getCurrentUserId();
        Task task = taskRepository.findByIdAndUserId(id, userId).orElseThrow(() -> new TaskNotFoundException(id));

        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setPriority(request.priority() != null ? request.priority() : task.getPriority());
        task.setDueDate(request.dueDate());
        task.setCategory(request.category());

        return toResponse(task);
    }

    public TaskResponse updateStatus(UUID id, String newStatus) {
        UUID userId = getCurrentUserId();
        Task task = taskRepository.findByIdAndUserId(id, userId).orElseThrow(() -> new TaskNotFoundException(id));

        task.setStatus(newStatus);
        if("DONE".equals(newStatus)) {
            task.setCompletedAt(Instant.now());
            eventPublisher.publishEvent(new TaskCompletedEvent(task.getId(), userId, task.getTitle()));
        } else {
            task.setCompletedAt(null);
        }

        return toResponse(task);
    }

    public void delete(UUID id) {
        UUID userId = getCurrentUserId();
        Task task = taskRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new TaskNotFoundException(id));
        taskRepository.delete(task);
    }

    private UUID getCurrentUserId() {
        return (UUID)  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getDueDate(),
                task.getCategory(),
                task.getCompletedAt(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}
