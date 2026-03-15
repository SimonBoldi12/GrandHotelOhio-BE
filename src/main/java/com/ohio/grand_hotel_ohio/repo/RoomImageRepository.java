package com.ohio.grand_hotel_ohio.repo;

import com.ohio.grand_hotel_ohio.entity.RoomImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomImageRepository extends JpaRepository<RoomImage, Long> {
}
