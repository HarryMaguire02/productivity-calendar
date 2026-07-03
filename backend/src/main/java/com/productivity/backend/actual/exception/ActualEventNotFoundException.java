package com.productivity.backend.actual.exception;

import java.util.UUID;

public class ActualEventNotFoundException extends RuntimeException {
    public ActualEventNotFoundException(UUID id) {
        super("Actual event not found: " + id);
    }
}
