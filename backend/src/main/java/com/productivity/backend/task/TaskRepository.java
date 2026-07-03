package com.productivity.backend.task;

import com.productivity.backend.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByUserId(UUID userId);
    List<Task> findByUserIdAndStatus(UUID userId, String status);
    List<Task> findByUserIdAndDueDateBefore(UUID userId, LocalDate date);
    Optional<Task> findByIdAndUserId(UUID id, UUID userId);
    List<Task> findByUserIdOrderByCreatedAtDesc(UUID userId);
    List<Task> findByUserIdAndStatusOrderByCreatedAtDesc(UUID userId, String status);
}
