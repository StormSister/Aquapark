package com.example.aquapark.controller;

import com.example.aquapark.model.User;
import com.example.aquapark.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        System.out.println("Received request to register user: " + user.getUsername());

        if (userService.existsByUsername(user.getUsername())) {
            System.out.println("Username " + user.getUsername() + " is already taken");
            return ResponseEntity.badRequest().body("Username is already taken!");
        }

        if (userService.existsByEmail(user.getEmail())) {
            System.out.println("Email " + user.getEmail() + " is already taken");
            return ResponseEntity.badRequest().body("Email is already taken!");
        }

        User savedUser = userService.saveUser(user);
        System.out.println("User " + savedUser.getUsername() + " registered successfully");
        return ResponseEntity.ok(savedUser);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        System.out.println("Received request to fetch all users");

        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String role) {
        System.out.println("Received request to search users with parameters: "
                + "username=" + username + ", firstName=" + firstName
                + ", lastName=" + lastName + ", phoneNumber=" + phoneNumber
                + ", role=" + role);

        List<User> users = userService.searchUsers(username, firstName, lastName, phoneNumber, role);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody User updatedUser) {
        System.out.println("Received request to update user with ID: " + userId);

        try {
            User updatedUserFromService = userService.updateUser(userId, updatedUser);
            System.out.println("User with ID " + userId + " updated successfully");
            return ResponseEntity.ok(updatedUserFromService);
        } catch (IllegalArgumentException e) {
            System.err.println("Error updating user: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            System.err.println("Error updating user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating user");
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        System.out.println("Received request to delete user with ID: " + id);

        userService.deleteUser(id);
        System.out.println("User with ID " + id + " deleted successfully");
        return ResponseEntity.noContent().build();
    }

    public boolean isPasswordChangeAllowed(User updatedUser, Optional<User> existingUserOptional) {
        User existingUser = existingUserOptional.orElseThrow(() -> new IllegalArgumentException("User not found"));

        return (updatedUser.getRole().equals("client") || updatedUser.getRole().equals("worker"))
                && updatedUser.getId().equals(existingUser.getId());
    }
}
