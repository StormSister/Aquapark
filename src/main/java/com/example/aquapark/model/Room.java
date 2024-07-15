package com.example.aquapark.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private int capacity;
    private int beds;
    private String description;
    private String imagePath;
    private double price;
}