package com.example.aquapark.controller;

import com.example.aquapark.controller.dto.ReservationRequest;
import com.example.aquapark.model.Reservation;
import com.example.aquapark.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "http://localhost:3000")
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<?> makeReservation(@RequestBody List<ReservationRequest> reservationRequests) {
        try {
            System.out.println("Received reservation requests: " + reservationRequests);
            reservationService.makeReservation(reservationRequests);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the reservation.");
        }
    }
}
