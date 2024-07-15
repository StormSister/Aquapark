package com.example.aquapark.service;

import com.example.aquapark.model.User;
import com.example.aquapark.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User saveUserNoLogin(User user) {
        return userRepository.save(user);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username) != null;
    }

    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public List<User> searchUsers(String username, String firstName, String lastName, String phoneNumber, String role) {
        return userRepository.searchUsers(username, firstName, lastName, phoneNumber, role);
    }

    public User updateUser(Long userId, User updatedUser) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();

            if (isPasswordChangeAllowed(existingUser, updatedUser)) {
                updatedUser.setPassword(bCryptPasswordEncoder.encode(updatedUser.getPassword()));
            } else {
                updatedUser.setPassword(existingUser.getPassword());
            }

            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setFirstName(updatedUser.getFirstName());
            existingUser.setLastName(updatedUser.getLastName());
            existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
            existingUser.setRole(updatedUser.getRole());

            return userRepository.save(existingUser);
        } else {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }
    }
    public boolean isPasswordChangeAllowed(User updatedUser, User existingUser) {
        return (updatedUser.getRole().equals("client") || updatedUser.getRole().equals("worker"))
                && updatedUser.getId().equals(existingUser.getId());
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);}}

