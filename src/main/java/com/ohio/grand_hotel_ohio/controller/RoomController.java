package com.ohio.grand_hotel_ohio.controller;

import com.ohio.grand_hotel_ohio.dto.RoomDTO;
import com.ohio.grand_hotel_ohio.entity.Room;
import com.ohio.grand_hotel_ohio.service.impl.RoomsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {
    private final RoomsServiceImpl roomsService;

    public RoomController(RoomsServiceImpl roomsService) {
        this.roomsService = roomsService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<RoomDTO>> getAllRooms() {
        List<RoomDTO> rooms = roomsService.getAllRooms();

        return ResponseEntity.ok(rooms);
    }

}
