package com.productivity.backend.calendar.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record PlannedEventRequest(
        @NotBlank String title,
        String description,
        @NotNull Instant startTime,
        @NotNull Instant endTime,
        @NotBlank String category,
        String color,
        String recurrenceRule
) {}
