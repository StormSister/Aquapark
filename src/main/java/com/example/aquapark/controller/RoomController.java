package com.example.aquapark.controller;

import com.example.aquapark.controller.dto.RoomTypeResponse;
import com.example.aquapark.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@CrossOrigin(origins = "http://localhost:3000")


@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/available")
    public ResponseEntity<List<RoomTypeResponse>> getAvailableRoomTypes(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        List<RoomTypeResponse> availableRoomTypes = roomService.getAvailableRoomTypes(startDate, endDate);
        System.out.println(availableRoomTypes);
        return ResponseEntity.ok(availableRoomTypes);
    }
}