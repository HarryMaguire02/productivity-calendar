package com.productivity.backend.auth.dto;

public record RegisterRequest (
        String name,
        String email,
        String password,
        String timezone
) {}
