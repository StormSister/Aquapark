
package com.example.aquapark.controller;

import com.example.aquapark.model.User;  // Import klasy User
import com.example.aquapark.service.AuthService;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private AuthService authService;

//    @Autowired
//    private GoogleAuthService googleAuthService;

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

//    @PostMapping("/api/google-login")
//    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> body) {
//        String idTokenString = body.get("token");
//        try {
//            GoogleIdToken.Payload payload = googleAuthService.verifyAndSaveUser(idTokenString);
//            return ResponseEntity.ok(payload);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
//        }
//    }
}