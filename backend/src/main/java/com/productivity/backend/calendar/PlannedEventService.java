package com.productivity.backend.calendar;

import com.productivity.backend.calendar.dto.PlannedEventRequest;
import com.productivity.backend.calendar.dto.PlannedEventResponse;
import com.productivity.backend.calendar.exception.EventNotFoundException;
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
public class PlannedEventService {

    private final PlannedEventRepository plannedEventRepository;
    private final UserRepository  userRepository;

    public PlannedEventResponse create(PlannedEventRequest request) {
        if (!request.endTime().isAfter(request.startTime())) {
            throw new IllegalArgumentException("end time must be after start time");
        }
        UUID userId = getCurrentUserId();
        User  user = userRepository.getReferenceById(userId);

        PlannedEvent event = new PlannedEvent();
        event.setUser(user);
        event.setTitle(request.title());
        event.setDescription(request.description());
        event.setStartTime(request.startTime());
        event.setEndTime(request.endTime());
        event.setCategory(request.category());
        event.setColor(request.color());
        event.setRecurrenceRule(request.recurrenceRule());

        plannedEventRepository.save(event);

        List<PlannedEventResponse.ConflictSummary> conflicts = findConflicts(userId, request.startTime(), request.endTime(),event.getId());

        return toResponse(event, conflicts);
    }

    @Transactional(readOnly = true)
    public List<PlannedEventResponse> list(Instant start, Instant end) {
        UUID userId = getCurrentUserId();
        return plannedEventRepository
                .findByUserIdAndStartTimeBetweenOrderByStartTimeAsc(userId, start, end)
                .stream()
                .map(e -> toResponse(e, List.of()))
                .toList();
    }

    @Transactional(readOnly = true)
    public PlannedEventResponse getById(UUID id) {
        UUID userId = getCurrentUserId();
        PlannedEvent event = plannedEventRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new EventNotFoundException(id));
        return toResponse(event, List.of());
    }

    public PlannedEventResponse update(UUID id, PlannedEventRequest request) {
        if (!request.endTime().isAfter(request.startTime())) {
            throw new IllegalArgumentException("End time must be after start time");
        }
        UUID userId = getCurrentUserId();
        PlannedEvent event = plannedEventRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new EventNotFoundException(id));

        event.setTitle(request.title());
        event.setDescription(request.description());
        event.setStartTime(request.startTime());
        event.setEndTime(request.endTime());
        event.setCategory(request.category());
        event.setColor(request.color());
        event.setRecurrenceRule(request.recurrenceRule());

        List<PlannedEventResponse.ConflictSummary> conflicts = findConflicts(userId, request.startTime(), request.endTime(), id);
        return toResponse(event, conflicts);
    }

    public void delete(UUID id) {
        UUID userId = getCurrentUserId();
        PlannedEvent plannedEvent = plannedEventRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new EventNotFoundException(id));
        plannedEventRepository.delete(plannedEvent);
    }

    private List<PlannedEventResponse.ConflictSummary> findConflicts(UUID userId, Instant start, Instant end, UUID excludeId) {
        return plannedEventRepository.findConflicts(userId, start, end, excludeId)
                .stream()
                .map(e -> new PlannedEventResponse.ConflictSummary(e.getId(), e.getTitle(), e.getStartTime(), e.getEndTime()))
                .toList();
    }

    private UUID getCurrentUserId() {
        return (UUID)  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private PlannedEventResponse toResponse(PlannedEvent event, List<PlannedEventResponse.ConflictSummary> conflicts) {
        return new PlannedEventResponse(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getStartTime(),
                event.getEndTime(),
                event.getCategory(),
                event.getColor(),
                event.getRecurrenceRule(),
                conflicts,
                event.getCreatedAt(),
                event.getUpdatedAt()
        );
    }

}
