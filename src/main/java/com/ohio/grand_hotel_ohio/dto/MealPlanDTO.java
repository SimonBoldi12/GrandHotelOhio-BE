package com.ohio.grand_hotel_ohio.dto;

import lombok.Data;

@Data
public class MealPlanDTO {
    private Long id;
    private String type;
    private String name;
    private Double pricePerNight;
}

