package com.ohio.grand_hotel_ohio.repo;

import com.ohio.grand_hotel_ohio.entity.HotelService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelServiceRepository extends JpaRepository<HotelService, Long> {
    List<HotelService> findByCategory(String category);
    List<HotelService> findByAvailableTrue();
    List<HotelService> findByCategoryAndAvailableTrue(String category);


}

