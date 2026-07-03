package com.productivity.backend.actual;

import com.productivity.backend.actual.dto.ActualEventRequest;
import com.productivity.backend.actual.dto.ActualEventResponse;
import com.productivity.backend.actual.exception.ActualEventNotFoundException;
import com.productivity.backend.calendar.PlannedEventRepository;
import com.productivity.backend.entity.ActualEvent;
import com.productivity.backend.entity.PlannedEvent;
import com.productivity.backend.entity.User;
import com.productivity.backend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ActualEventService {

    private final ActualEventRepository actualEventRepository;
    private final UserRepository userRepository;
    private final PlannedEventRepository plannedEventRepository;

    public ActualEventResponse create(ActualEventRequest request) {
        if (!request.endTime().isAfter(request.startTime())) {
            throw new IllegalArgumentException("End time must be after start time");
        }
        UUID userId = getCurrentUserId();
        User user = userRepository.getReferenceById(userId);

        ActualEvent event = new ActualEvent();
        event.setUser(user);
        event.setTitle(request.title());
        event.setDescription(request.description());
        event.setStartTime(request.startTime());
        event.setEndTime(request.endTime());
        event.setCategory(request.category());
        event.setMood(request.mood());
        event.setLinkedPlannedEvent(resolveLinkedEvent(request.linkedPlannedEventId(), userId));

        actualEventRepository.save(event);
        return toResponse(event);
    }

    @Transactional(readOnly = true)
    public List<ActualEventResponse> list(Instant start, Instant end) {
        UUID userId = getCurrentUserId();
        return actualEventRepository
                .findByUserIdAndStartTimeBetweenOrderByStartTimeAsc(userId, start, end)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ActualEventResponse getById(UUID id) {
        UUID userId = getCurrentUserId();
        ActualEvent event = actualEventRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ActualEventNotFoundException(id));
        return toResponse(event);
    }

    public ActualEventResponse update(UUID id, ActualEventRequest request) {
        if (!request.endTime().isAfter(request.startTime())) {
            throw new IllegalArgumentException("End time must be after start time");
        }
        UUID userId = getCurrentUserId();
        ActualEvent event = actualEventRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ActualEventNotFoundException(id));

        event.setTitle(request.title());
        event.setDescription(request.description());
        event.setStartTime(request.startTime());
        event.setEndTime(request.endTime());
        event.setCategory(request.category());
        event.setMood(request.mood());
        event.setLinkedPlannedEvent(resolveLinkedEvent(request.linkedPlannedEventId(), userId));

        return toResponse(event);
    }

    public void delete(UUID id) {
        UUID userId = getCurrentUserId();
        ActualEvent event = actualEventRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ActualEventNotFoundException(id));
        actualEventRepository.delete(event);
    }

    private PlannedEvent resolveLinkedEvent(UUID linkedId, UUID userId) {
        if (linkedId == null) return null;
        return plannedEventRepository.findByIdAndUserId(linkedId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Linked planned event not found: " + linkedId));
    }

    private UUID getCurrentUserId() {
        return (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private ActualEventResponse toResponse(ActualEvent event) {
        UUID linkedId = event.getLinkedPlannedEvent() != null
                ? event.getLinkedPlannedEvent().getId()
                : null;
        return new ActualEventResponse(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getStartTime(),
                event.getEndTime(),
                event.getCategory(),
                linkedId,
                event.getMood(),
                event.getCreatedAt(),
                event.getUpdatedAt()
        );
    }
}
