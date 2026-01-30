package com.ohio.grand_hotel_ohio.service.impl;

import com.ohio.grand_hotel_ohio.dto.RoomDTO;
import com.ohio.grand_hotel_ohio.repo.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomsServiceImpl {
    private final RoomRepository roomRepository;

    public RoomsServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<RoomDTO> getAllRooms() {
        return roomRepository.findAllRooms()
                .stream()
                .map(r ->
                        new RoomDTO(
                                r.getId(),
                                r.getRoomDescription(),
                                r.getRoomPrice(),
                                r.getRoomPhotoUrl(),
                                r.getRoomType())).toList();
    }

}
