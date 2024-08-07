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

import java.time.LocalDate;
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

    public List<Reservation> getUserReservationsByEmail(String email) {
        return reservationRepository.findByUserEmail(email);
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
    public void cancelReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    public void updateReservation(Long id, ReservationRequest reservationRequest) throws ReservationException {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new ReservationException("Reservation not found"));
        List<Room> availableRooms = roomRepository.findAvailableRoomsByType(reservationRequest.getStartDate(), reservationRequest.getEndDate(), reservationRequest.getRoomType());

        if (availableRooms.isEmpty()) {
            throw new ReservationException("No available rooms of type " + reservationRequest.getRoomType() + " in the selected dates.");
        }

        Room room = availableRooms.get(0);
        reservation.setRoom(room);
        reservation.setStartDate(reservationRequest.getStartDate());
        reservation.setEndDate(reservationRequest.getEndDate());
        reservationRepository.save(reservation);
    }

    public List<Reservation> searchReservations(String startDate, String endDate) {
        if (startDate != null && endDate != null) {

            return reservationRepository.findByDateRange(LocalDate.parse(startDate), LocalDate.parse(endDate));
        } else {
            return reservationRepository.findAll();
        }
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }
}
