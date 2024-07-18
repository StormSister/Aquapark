package com.example.aquapark.controller.dto;


import lombok.Data;

@Data
public class PriceDTO {
    private Long id;
    private String type;
    private String category;
    private double price;
}