package com.productivity.backend.shared;

import java.time.Instant;
import java.util.List;

public record ApiError (
        String error,
        List<String> details,
        Instant timestamp
) { }
