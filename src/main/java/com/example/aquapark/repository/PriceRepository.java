package com.example.aquapark.repository;

import com.example.aquapark.model.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {

    // Method to find a Price by type and category
    Price findByTypeAndCategory(String type, String category);

    // You can define more methods if needed based on your requirements

}