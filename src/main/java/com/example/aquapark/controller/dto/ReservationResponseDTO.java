package com.example.aquapark.controller.dto;


import lombok.Data;

import java.time.LocalDate;
@Data
public class ReservationResponseDTO {
    private Long id;
    private String userEmail;
    private String userName;
    private String roomType;
    private LocalDate startDate;
    private LocalDate endDate;

}
