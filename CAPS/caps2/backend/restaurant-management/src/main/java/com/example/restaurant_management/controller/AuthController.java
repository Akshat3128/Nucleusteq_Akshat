package com.example.restaurant_management.controller;

import com.example.restaurant_management.model.User;
import com.example.restaurant_management.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // 🔹 Register a new user with proper success/failure messages
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        try {
            User user = authService.registerUser(request.get("email"), request.get("password"), request.get("role"));
            return ResponseEntity.ok(Map.of(
                "message", "Registration successful!",
                "userId", user.getId(),
                "email", user.getEmail(),
                "role", user.getRole()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Registration failed!",
                "message", e.getMessage()
            ));
        }
    }

    // 🔹 Login with email & password 
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        try {
            boolean authenticated = authService.authenticateUser(request.get("email"), request.get("password"));
            if (authenticated) {
                return ResponseEntity.ok(Map.of(
                    "message", "Login successful!",
                    "email", request.get("email")
                ));
            } else {
                return ResponseEntity.status(401).body(Map.of(
                    "error", "Invalid credentials!",
                    "message", "Please check your email and password."
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "Login failed!",
                "message", e.getMessage()
            ));
        }
    }
}
