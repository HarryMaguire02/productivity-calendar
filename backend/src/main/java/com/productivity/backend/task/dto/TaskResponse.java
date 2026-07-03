package com.productivity.backend.task.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record TaskResponse(
        UUID id,
        String title,
        String description,
        String status,
        String priority,
        LocalDate dueDate,
        String category,
        Instant completedAt,
        Instant createdAt,
        Instant updatedAt
) {}
