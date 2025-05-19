package com.example.demo.controller;

import com.example.demo.service.HrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hr")
// @CrossOrigin(origins = "*")
public class HrController {

    @Autowired
    private HrService hrService;

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password) {
        try {
            if (hrService.authenticate(email, password)) {
                return "Login successful";
            } else {
                return "Invalid credentials";
            }
        } catch (Exception e) {
            e.printStackTrace(); // for debugging (optional)
            return "An error occurred during login: " + e.getMessage();
        }
    }

    @PostMapping("/register")
    public String register(@RequestParam String email, @RequestParam String password) {
        try {
            hrService.registerHr(email, password);
            return "HR registered";
        } catch (Exception e) {
            e.printStackTrace(); 
            return "An error occurred during registration: " + e.getMessage();
        }
    }
}
