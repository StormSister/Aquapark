package com.example.aquapark.controller.dto;

import com.example.aquapark.model.User;
import lombok.Data;

import java.time.LocalDate;


@Data
public class ReservationRequest {
    private String roomType;
    private LocalDate startDate;
    private LocalDate endDate;
    private int numberOfPersons;
    private User user;
}
