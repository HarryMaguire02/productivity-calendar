package com.productivity.backend.auth.dto;

public record AuthResponse (
  String token,
  String email,
  String name
) {}
