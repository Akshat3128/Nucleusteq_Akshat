package com.example.demo.controller;

import com.example.demo.service.HrService;
// import com.example.demo.repository.HrRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hr")
// @CrossOrigin(origins = "*")
public class HrController {
    @Autowired
    private HrService hrService;

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password) {

        if (!email.toLowerCase().endsWith("@hr.com")) {
            return "Email must end with @hr.com";
        }

        if (hrService.authenticate(email, password)) {
            return "Login successful";
        } else {
            return "Invalid credentials";
        }
    }

    // i have added reg option in backend but not implemented in fronted as it was not stated in srs but for convenience we can add or register an hr from our side without giving user the right to reg as an hr  for demo we have admin@hr.com as user and admin@123 as password 
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String email, @RequestParam String password) {
        if (!email.toLowerCase().endsWith("@hr.com")) {
            return ResponseEntity.badRequest().body("Email must end with @hr.com");
        }

        // if (password.length() < 6) {
        //     return ResponseEntity.badRequest().body("Password must be at least 6 characters");
        // }

        // Optional: check if HR already exists
        boolean exists = hrService.authenticate(email, password);
        if (exists) {
            return ResponseEntity.badRequest().body("HR already registered");
        }

        hrService.registerHr(email, password);
        return ResponseEntity.ok("HR registered successfully");
    }

}