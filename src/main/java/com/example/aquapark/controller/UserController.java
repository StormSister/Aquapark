package com.example.aquapark.controller;

import com.example.aquapark.model.User;
import com.example.aquapark.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (userService.existsByUsername(user.getUsername())) {
            System.out.println("Registration failed: Username is already taken");
            return ResponseEntity.badRequest().body("Username is already taken!");
        }
        if (userService.existsByEmail(user.getEmail())) {
            System.out.println("Registration failed: Email is already taken");
            return ResponseEntity.badRequest().body("Email is already taken!");
        }
        User savedUser = userService.saveUser(user);
        System.out.println("Registration successful for user: " + savedUser.getUsername());
        return ResponseEntity.ok(savedUser);
    }

    @GetMapping
    public List<User> getAllUsers() {
        List<User> users = userService.findAllUsers();
        System.out.println("Retrieved all users successfully");
        return users;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsersResponseEntity() {
        try {
            List<User> users = userService.findAllUsers();
            System.out.println("Retrieved all users successfully");
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            System.err.println("Error retrieving all users: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    public List<User> searchUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String role) {
        List<User> users = userService.searchUsers(username, firstName, lastName, phoneNumber, role);
        System.out.println("Search users successful with parameters: username=" + username +
                ", firstName=" + firstName + ", lastName=" + lastName +
                ", phoneNumber=" + phoneNumber + ", role=" + role);
        return users;
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody User userToUpdate) {
        try {

            if (userToUpdate.getPassword() != null) {
                throw new IllegalArgumentException("User is not allowed to change the password.");
            }


            User updatedUser = userService.updateUser(userToUpdate);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {

            System.err.println("Error updating user: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {

            System.err.println("Error updating user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating user");
        }
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        System.out.println("User deleted successfully with id: " + id);
    }
}
