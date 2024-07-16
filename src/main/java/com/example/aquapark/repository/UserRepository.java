package com.example.aquapark.repository;


import com.example.aquapark.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByEmail(String email);


    @Query("SELECT u FROM User u WHERE " +
            "(:email IS NULL OR u.email LIKE %:email%) AND " +
            "(:username IS NULL OR u.username LIKE %:username%) AND " +
            "(:firstName IS NULL OR u.firstName LIKE %:firstName%) AND " +
            "(:lastName IS NULL OR u.lastName LIKE %:lastName%) AND " +
            "(:phoneNumber IS NULL OR u.phoneNumber LIKE %:phoneNumber%) AND " +
            "(:role IS NULL OR u.role = :role)")
    List<User> searchUsers(@Param("email") String email,
                           @Param("username") String username,
                           @Param("firstName") String firstName,
                           @Param("lastName") String lastName,
                           @Param("phoneNumber") String phoneNumber,
                           @Param("role") String role);
}