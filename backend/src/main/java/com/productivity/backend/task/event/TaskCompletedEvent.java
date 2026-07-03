package com.productivity.backend.task.event;

import java.util.UUID;

public record TaskCompletedEvent(UUID taskId, UUID userId, String taskTitle) {}
