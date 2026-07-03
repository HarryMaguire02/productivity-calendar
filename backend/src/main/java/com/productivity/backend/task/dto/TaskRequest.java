package com.productivity.backend.task.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record TaskRequest(
        @NotBlank String title,
        String description,
        String priority,
        LocalDate dueDate,
        String category
) {}
