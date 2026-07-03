package com.productivity.backend.task.dto;

import jakarta.validation.constraints.NotBlank;

public record TaskStatusRequest(@NotBlank String status) {}
