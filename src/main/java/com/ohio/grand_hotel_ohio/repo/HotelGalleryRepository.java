package com.ohio.grand_hotel_ohio.repo;

import com.ohio.grand_hotel_ohio.entity.HotelGallery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelGalleryRepository extends JpaRepository<HotelGallery, Long> {
    List<HotelGallery> findByCategory(String category);
    List<HotelGallery> findByOrderByCategoryAsc();
}

