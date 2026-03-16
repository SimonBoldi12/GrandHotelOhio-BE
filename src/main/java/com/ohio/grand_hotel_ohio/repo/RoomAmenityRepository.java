package com.ohio.grand_hotel_ohio.repo;

import com.ohio.grand_hotel_ohio.entity.RoomAmenity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomAmenityRepository extends JpaRepository<RoomAmenity, Long> {
    List<RoomAmenity> findByRoomId(Long roomId);
    void deleteByRoomId(Long roomId);

}