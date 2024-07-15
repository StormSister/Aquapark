package com.example.aquapark.service;

import com.example.aquapark.model.Price;
import com.example.aquapark.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PriceService {

    @Autowired
    private PriceRepository priceRepository;

    public Map<String, Double> getRoomTypePrices() {
        List<Price> roomPrices = priceRepository.findByType("Room");
        return roomPrices.stream()
                .collect(Collectors.toMap(Price::getCategory, Price::getValue));
    }
}
