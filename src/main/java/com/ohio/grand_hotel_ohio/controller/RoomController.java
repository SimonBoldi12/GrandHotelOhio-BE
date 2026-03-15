package com.ohio.grand_hotel_ohio.controller;

import com.ohio.grand_hotel_ohio.dto.Response;
import com.ohio.grand_hotel_ohio.service.interfac.IBookingService;
import com.ohio.grand_hotel_ohio.service.interfac.IRoomService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    public final IRoomService roomService;
    public final IBookingService bookingService;

    public RoomController(IRoomService roomService, IBookingService bookingService) {
        this.roomService = roomService;
        this.bookingService = bookingService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> addNewRoom(
            @RequestParam(value = "photo", required = false)MultipartFile photo,
            @RequestParam(value = "roomType", required = false)String roomType,
            @RequestParam(value = "roomPrice", required = false) Integer roomPrice,
            @RequestParam(value = "roomDescription", required = false)String roomDescription
            ){
        if(photo == null || photo.isEmpty() || roomType == null || roomType.isBlank() || roomPrice == null){
            Response response = new Response();
            response.setStatus(400);
            response.setMessage("Please provide values for all fields(photo, roomType, roomPrice)");
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        Response response = roomService.addNewRoom(photo, roomType, roomPrice, roomDescription);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllRooms(){
        Response response = roomService.getAllRooms();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/types")
    public List<String> getRoomTypes(){
        return roomService.getAllRoomTypes();
    }

    @GetMapping("/room-by-id/{roomId}")
    public ResponseEntity<Response> getRoomById(@PathVariable Long roomId){
        Response response = roomService.getRoomById(roomId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/all-available-rooms")
    public ResponseEntity<Response> getAvailableRooms(){
        Response response = roomService.getAllAvailableRooms();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/available-rooms-by-date-and-type")
    public ResponseEntity<Response> getAvailableRoomsByDataAndType(
            @RequestParam(required = false)@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam(required = false)@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @RequestParam(required = false) String roomType
    ){
        if(checkInDate == null || roomType == null || roomType.isBlank() || checkOutDate == null){
            Response response = new Response();
            response.setStatus(400);
            response.setMessage("Please provide values for all fields(checkInDate, checkOutDate, roomType)");
            return ResponseEntity.status(response.getStatus()).body(response);
        }

        Response response = roomService.getAvailableRoomsByDataAndType(checkInDate, checkOutDate, roomType);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/update/{roomId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> updateRoom(
            @PathVariable Long roomId,
            @RequestParam(value = "photo", required = false)MultipartFile photo,
            @RequestParam(value = "roomType", required = false)String roomType,
            @RequestParam(value = "roomPrice", required = false) Integer roomPrice,
            @RequestParam(value = "roomDescription", required = false)String roomDescription
    ){
        Response response = roomService.updateRoom(roomId, roomType, roomPrice, photo, roomDescription);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/delete/{roomId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> deleteRoom(@PathVariable Long roomId){
        Response response = roomService.deleteRoom(roomId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/{roomId}/add-image")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> addImageToRoom(
            @PathVariable Long roomId,
            @RequestParam("photo") MultipartFile photo) {
        Response response = roomService.addImageToRoom(roomId, photo);
        return ResponseEntity.status(response.getStatus()).body(response);
    }


}

