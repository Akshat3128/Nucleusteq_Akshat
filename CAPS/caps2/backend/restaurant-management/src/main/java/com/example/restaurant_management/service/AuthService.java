package com.example.restaurant_management.service;

import com.example.restaurant_management.model.Role;
import com.example.restaurant_management.model.User;
import com.example.restaurant_management.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; 

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Register a new user
    public User registerUser(String email, String password, String role) {
        // Checking if email is already registered
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("User with this email already exists.");
        }

        Role userRole;
        try {
            userRole = Role.valueOf(role.toUpperCase()); // Convert to ENUM
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role. Allowed values: OWNER, CUSTOMER");
        }

        // Hashing password before saving
        String encodedPassword = passwordEncoder.encode(password);
        
        User user = new User(email, encodedPassword, userRole);
        return userRepository.save(user);
    }

    // 🔹 Authenticate user with email & password (Basic Login)
    public boolean authenticateUser(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent() && passwordEncoder.matches(password, user.get().getPassword());
    }
}
