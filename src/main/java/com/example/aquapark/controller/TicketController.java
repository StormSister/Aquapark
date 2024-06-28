package com.example.aquapark.controller;

import com.example.aquapark.model.Ticket;
import com.example.aquapark.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/purchase")
    public String purchaseTickets(@RequestBody Map<String, Object> requestData) {
        String email = (String) requestData.get("email");
        int adults = (int) requestData.get("adults");
        int children = (int) requestData.get("children");
        boolean isGroup = (boolean) requestData.get("isGroup");

        // Logging received data
        System.out.println("Received purchase request in controller:");
        System.out.println("Email: " + email);
        System.out.println("Adults: " + adults);
        System.out.println("Children: " + children);
        System.out.println("IsGroup: " + isGroup);

        ticketService.purchaseTickets(email, adults, children, isGroup);

        return "Tickets purchased successfully. They have been sent to your email.";
    }




    @GetMapping("/user/tickets")
    public List<String> getUserTicketPaths(@RequestParam("email") String userEmail) {
        System.out.println("Fetching ticket paths for user: " + userEmail);

        List<String> pdfPaths = ticketService.getUserTicketPath(userEmail);

        System.out.println("Found ticket paths: " + pdfPaths);

        // Assuming ticket paths are relative to some base URL
        List<String> pdfUrls = pdfPaths.stream()
                .map(path -> "http://localhost:8080/api/tickets/" + path)
                .collect(Collectors.toList());
        System.out.println("Found ticket paths: " + pdfUrls);

        return pdfUrls;
    }
}