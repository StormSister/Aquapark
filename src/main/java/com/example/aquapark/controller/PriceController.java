package com.example.aquapark.controller;

import com.example.aquapark.controller.dto.PriceDTO;
import com.example.aquapark.model.Price;
import com.example.aquapark.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/prices")
@CrossOrigin(origins = "http://localhost:3000")
public class PriceController {

    private final PriceService priceService;

    @Autowired
    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @GetMapping
    public ResponseEntity<List<Price>> getAllPrices() {
        List<Price> prices = priceService.getAllPrices();
        return ResponseEntity.ok(prices);
    }

//    @GetMapping("/search")
//    public ResponseEntity<List<PriceDTO>> searchPrices(
//            @RequestParam(value = "type", required = false) String type,
//            @RequestParam(value = "category", required = false) String category) {
//
//        List<Price> foundPrices;
//
//        if (type != null && category != null) {
//            foundPrices = priceService.findByTypeAndCategory(type, category);
//        } else if (type != null) {
//            foundPrices = priceService.findByType(type);
//        } else if (category != null) {
//            foundPrices = priceService.findByCategory(category);
//        } else {
//            // Zwróć wszystkie ceny, gdy żaden parametr nie jest podany
//            foundPrices = priceService.getAllPrices();
//        }
//
//        List<PriceDTO> foundPriceDTOs = foundPrices.stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//        return ResponseEntity.ok(foundPriceDTOs);
//    }

    @PostMapping("/add")
    public ResponseEntity<PriceDTO> addPrice(@RequestBody PriceDTO priceDTO) {
        Price price = convertToEntity(priceDTO);
        Price savedPrice = priceService.addPrice(price);
        PriceDTO savedPriceDTO = convertToDTO(savedPrice);
        return ResponseEntity.ok(savedPriceDTO);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PriceDTO> updatePrice(@PathVariable Long id, @RequestBody PriceDTO priceDTO) {
        Price price = convertToEntity(priceDTO);
        Price updatedPrice = priceService.updatePrice(id, price);
        PriceDTO updatedPriceDTO = convertToDTO(updatedPrice);
        return ResponseEntity.ok(updatedPriceDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePrice(@PathVariable Long id) {
        priceService.deletePrice(id);
        return ResponseEntity.noContent().build();
    }

    private PriceDTO convertToDTO(Price price) {
        PriceDTO priceDTO = new PriceDTO();
        priceDTO.setId(price.getId());
        priceDTO.setType(price.getType());
        priceDTO.setCategory(price.getCategory());
        priceDTO.setPrice(price.getPrice());
        return priceDTO;
    }

    private Price convertToEntity(PriceDTO priceDTO) {
        Price price = new Price();
        price.setId(priceDTO.getId());
        price.setType(priceDTO.getType());
        price.setCategory(priceDTO.getCategory());
        price.setPrice(priceDTO.getPrice());
        return price;
    }
}
