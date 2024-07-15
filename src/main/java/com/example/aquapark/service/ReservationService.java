package com.example.aquapark.service;

import com.example.aquapark.controller.dto.ReservationRequest;
import com.example.aquapark.controller.exceptions.ReservationException;
import com.example.aquapark.model.Reservation;
import com.example.aquapark.model.Room;
import com.example.aquapark.model.User;
import com.example.aquapark.repository.ReservationRepository;
import com.example.aquapark.repository.RoomRepository;
import com.example.aquapark.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;



@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, RoomRepository roomRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    public void makeReservation(List<ReservationRequest> reservationRequests) throws ReservationException {
        for (ReservationRequest request : reservationRequests) {
            System.out.println("Processing reservation request: " + request);
            List<Room> availableRooms = roomRepository.findAvailableRoomsByType(request.getStartDate(), request.getEndDate(), request.getRoomType());

            if (availableRooms.isEmpty()) {
                throw new ReservationException("No available rooms of type " + request.getRoomType() + " in the selected dates.");
            }

            User user = userRepository.findByEmail(request.getUser().getEmail());
            if (user == null) {
                user = new User();
                user.setFirstName(request.getUser().getFirstName());
                user.setLastName(request.getUser().getLastName());
                user.setEmail(request.getUser().getEmail());
                user.setPhoneNumber(request.getUser().getPhoneNumber());
                user.setRole("client");
                userRepository.save(user);
            }

            // Reserve only the quantity specified by the request
            int quantity = request.getNumberOfPersons();
            for (int i = 0; i < quantity && i < availableRooms.size(); i++) {
                Room room = availableRooms.get(i);
                Reservation reservation = new Reservation();
                reservation.setRoom(room);
                reservation.setUser(user);
                reservation.setStartDate(request.getStartDate());
                reservation.setEndDate(request.getEndDate());
                reservationRepository.save(reservation);
            }
        }
    }
}
