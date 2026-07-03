package com.productivity.backend.actual.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record ActualEventRequest(
        @NotBlank String title,
        String description,
        @NotNull Instant startTime,
        @NotNull Instant endTime,
        @NotBlank String category,
        UUID linkedPlannedEventId,
        @Min(1) @Max(5) Short mood
) {}
