package com.productivity.backend.calendar.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record PlannedEventResponse(
        UUID id,
        String title,
        String description,
        Instant startTime,
        Instant endTime,
        String category,
        String color,
        String recurrenceRule,
        List<ConflictSummary> conflicts,
        Instant createdAt,
        Instant updatedAt
) {
    public record ConflictSummary(UUID id, String title, Instant startTime, Instant endTime) {}
}
