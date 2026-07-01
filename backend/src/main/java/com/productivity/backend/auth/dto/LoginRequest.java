package com.productivity.backend.auth.dto;

public record LoginRequest (
        String email,
        String password
) {}
