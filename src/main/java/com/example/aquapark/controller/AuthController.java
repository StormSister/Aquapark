package com.example.aquapark.controller;

import com.example.aquapark.model.User;
import com.example.aquapark.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/api/login")
    public ResponseEntity<?> login(@RequestBody User loginUser) {
        User user = authService.login(loginUser.getUsername(), loginUser.getPassword());
        if (user != null) {
            System.out.println("User logged in successfully: " + user.getUsername());
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/api/logout")
    public ResponseEntity<?> logout() {
        System.out.println("User logged out successfully.");
        return ResponseEntity.ok("Logged out successfully.");
    }
}