package com.productivity.backend.auth;


import com.productivity.backend.auth.dto.AuthResponse;
import com.productivity.backend.auth.dto.LoginRequest;
import com.productivity.backend.auth.dto.RegisterRequest;
import com.productivity.backend.entity.User;
import com.productivity.backend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService  jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse register (RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();
        user.setEmail(registerRequest.email());
        user.setPasswordHash(passwordEncoder.encode(registerRequest.password()));
        user.setName(registerRequest.name());
        user.setTimezone(registerRequest.timezone() != null ? registerRequest.timezone() : "UTC");

        userRepository.save(user);
        String token = jwtService.generateToken(user.getId(),user.getEmail());

        return new AuthResponse(token, user.getEmail(), user.getName());
    }

    public AuthResponse login (LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.email()).orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!passwordEncoder.matches(loginRequest.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        String token = jwtService.generateToken(user.getId(),user.getEmail());
        return new AuthResponse(token, user.getEmail(), user.getName());
    }
}
