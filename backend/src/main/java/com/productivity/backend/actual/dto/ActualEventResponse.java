package com.productivity.backend.actual.dto;

import java.time.Instant;
import java.util.UUID;

public record ActualEventResponse(
        UUID id,
        String title,
        String description,
        Instant startTime,
        Instant endTime,
        String category,
        UUID linkedPlannedEventId,
        Short mood,
        Instant createdAt,
        Instant updatedAt
) {}
