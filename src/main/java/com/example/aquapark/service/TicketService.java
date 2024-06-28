package com.example.aquapark.service;

import com.example.aquapark.model.Price;
import com.example.aquapark.model.Ticket;
import com.example.aquapark.repository.PriceRepository;
import com.example.aquapark.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class TicketService {

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private PdfService pdfService;

    @Transactional
    public void purchaseTickets(String email, int adults, int children, boolean isGroup) {
        LocalDateTime purchaseDate = LocalDateTime.now();
        LocalDateTime expirationDate = purchaseDate.plusDays(31);

        System.out.println("Received purchase request:");
        System.out.println("Email: " + email);
        System.out.println("Adults: " + adults);
        System.out.println("Children: " + children);
        System.out.println("IsGroup: " + isGroup);

        if (isGroup) {
            createGroupTicket(email, adults, children, purchaseDate, expirationDate);
        } else {
            createIndividualTickets(email, adults, children, purchaseDate, expirationDate);
        }

        // Placeholder for payment processing and sending email with PDF tickets
    }

    private void createGroupTicket(String email, int adults, int children, LocalDateTime purchaseDate, LocalDateTime expirationDate) {
        double price = calculateGroupPrice(adults, children);
        Ticket ticket = new Ticket();
        ticket.setEmail(email);
        ticket.setType("Group");
        ticket.setPrice(price);
        ticket.setPurchaseDate(purchaseDate);
        ticket.setExpirationDate(expirationDate);
        ticket.setQrCode(generateQrCode());
        ticket.setAdults(adults);
        ticket.setChildren(children);
        saveTicket(ticket);
    }

    private void createIndividualTickets(String email, int adults, int children, LocalDateTime purchaseDate, LocalDateTime expirationDate) {
        double adultPrice = ((Price) priceRepository.findByTypeAndCategory("Ticket", "Standard")).getValue();
        double childPrice = ((Price) priceRepository.findByTypeAndCategory("Ticket", "Child")).getValue();

        for (int i = 0; i < adults; i++) {
            Ticket ticket = new Ticket();
            ticket.setEmail(email);
            ticket.setType("Standard");
            ticket.setPrice(adultPrice);
            ticket.setPurchaseDate(purchaseDate);
            ticket.setExpirationDate(expirationDate);
            ticket.setQrCode(generateQrCode());
            ticket.setAdults(1);
            ticket.setChildren(0);
            saveTicket(ticket);
        }

        for (int i = 0; i < children; i++) {
            Ticket ticket = new Ticket();
            ticket.setEmail(email);
            ticket.setType("Child");
            ticket.setPrice(childPrice);
            ticket.setPurchaseDate(purchaseDate);
            ticket.setExpirationDate(expirationDate);
            ticket.setQrCode(generateQrCode());
            ticket.setAdults(0);
            ticket.setChildren(1);
            saveTicket(ticket);
        }
    }

    private void saveTicket(Ticket ticket) {
        ticket.setPdfPath("path/to/pdf");
        ticket.setStatus("active");

        System.out.println("Saving ticket:");
        System.out.println("Ticket ID: " + ticket.getId());
        System.out.println("Email: " + ticket.getEmail());
        System.out.println("Type: " + ticket.getType());
        System.out.println("Price: " + ticket.getPrice());
        System.out.println("Purchase Date: " + ticket.getPurchaseDate());
        System.out.println("Expiration Date: " + ticket.getExpirationDate());
        System.out.println("Adults: " + ticket.getAdults());
        System.out.println("Children: " + ticket.getChildren());
        System.out.println("QR Code: " + ticket.getQrCode());

        ticketRepository.save(ticket);

        try {
            String pdfPath = pdfService.generateTicketPdf(ticket);
            ticket.setPdfPath(pdfPath);
            ticketRepository.save(ticket);

            System.out.println("PDF generated successfully. Path: " + pdfPath);
        } catch (IOException e) {
            // Handle IOException
            e.printStackTrace();
        }
    }

    private double calculateGroupPrice(int adults, int children) {
        double adultPrice = priceRepository.findByTypeAndCategory("Ticket", "Standard").getValue();
        double childPrice = priceRepository.findByTypeAndCategory("Ticket", "Child").getValue();
        return (adults * adultPrice) + (children * childPrice);
    }

    private String generateQrCode() {
        return UUID.randomUUID().toString();
    }

    public List<Ticket> getUserTickets(Long userId) {
        return ticketRepository.findByUserId(userId);
    }

    public List<Ticket> getUserTickets(String email) {
        System.out.println("Fetching tickets for email: " + email);
        List<Ticket> tickets = ticketRepository.findByEmail(email);
        System.out.println("Found tickets: " + tickets.size());
        return tickets;
    }

    public List<String> getUserTicketPath(String email) {
        List<Ticket> tickets = ticketRepository.findByEmail(email); // Pobierz listę obiektów Ticket dla danego email

        List<String> pdfPaths = tickets.stream()
                .map(Ticket::getPdfPath) // Mapowanie Ticket na jego ścieżkę do PDF
                .collect(Collectors.toList());

        return pdfPaths;
    }
}