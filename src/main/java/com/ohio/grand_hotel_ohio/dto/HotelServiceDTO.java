package com.ohio.grand_hotel_ohio.dto;

import lombok.Data;

@Data
public class HotelServiceDTO {
    private Long id;
    private String category;
    private String name;
    private String description;
    private Double price;
    private String photoUrl;
    private Boolean available;
}

