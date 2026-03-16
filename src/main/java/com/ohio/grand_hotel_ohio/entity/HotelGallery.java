package com.ohio.grand_hotel_ohio.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Getter
@Setter
@Table(name = "hotel_gallery")
public class HotelGallery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String category;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column
    private String caption;

    public HotelGallery() {
    }

    public HotelGallery(String category, String imageUrl, String caption) {
        this.category = category;
        this.imageUrl = imageUrl;
        this.caption = caption;
    }
}

