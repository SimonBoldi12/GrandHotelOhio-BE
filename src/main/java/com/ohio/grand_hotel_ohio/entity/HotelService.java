package com.ohio.grand_hotel_ohio.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(name = "hotel_services")
@Getter
@Setter
public class HotelService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(name = "photo_url",nullable = false)
    private String photoUrl;

    @Column(nullable = false)
    private Boolean available = true;

    public HotelService() {
    }

    public HotelService(String category, String name, String description, Double price, String photoUrl, Boolean available) {
        this.category = category;
        this.name = name;
        this.description = description;
        this.price = price;
        this.photoUrl = photoUrl;
        this.available = available;
    }
}