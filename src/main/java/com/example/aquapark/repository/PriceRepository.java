package com.example.aquapark.repository;

import com.example.aquapark.model.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {
    List<Price> findByType(String type);

    Price findByTypeAndCategory(String type, String category);


}