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

    private final IRoomService roomService;
    private final IBookingService bookingService;

    public RoomController(IRoomService roomService, IBookingService bookingService) {
        this.roomService = roomService;
        this.bookingService = bookingService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<Response> addNewRoom(
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            @RequestParam(value = "roomType", required = false) String roomType,
            @RequestParam(value = "roomPrice", required = false) Integer roomPrice,
            @RequestParam(value = "roomDescription", required = false) String roomDescription) {
        if (photo == null || photo.isEmpty() || roomType == null || roomType.isBlank() || roomPrice == null) {
            Response response = new Response();
            response.setStatus(400);
            response.setMessage("Kérjük, töltse ki az összes mezőt (photo, roomType, roomPrice)");
            return ResponseEntity.status(400).body(response);
        }
        return ResponseEntity.ok(roomService.addNewRoom(photo, roomType, roomPrice, roomDescription));
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @GetMapping("/types")
    public List<String> getRoomTypes() {
        return roomService.getAllRoomTypes();
    }

    @GetMapping("/room-by-id/{roomId}")
    public ResponseEntity<Response> getRoomById(@PathVariable Long roomId) {
        return ResponseEntity.ok(roomService.getRoomById(roomId));
    }

    @GetMapping("/all-available-rooms")
    public ResponseEntity<Response> getAvailableRooms() {
        return ResponseEntity.ok(roomService.getAllAvailableRooms());
    }

    @GetMapping("/available-rooms-by-date-and-type")
    public ResponseEntity<Response> getAvailableRoomsByDataAndType(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @RequestParam(required = false) String roomType) {
        if (checkInDate == null || checkOutDate == null) {
            Response response = new Response();
            response.setStatus(400);
            response.setMessage("Kérem adja meg a be- és kijelentkezési dátumot.");
            return ResponseEntity.status(400).body(response);
        }
        if (roomType == null || roomType.isBlank()) {
            return ResponseEntity.ok(roomService.getAllAvailableRooms(checkInDate, checkOutDate));
        }
        return ResponseEntity.ok(roomService.getAvailableRoomsByDataAndType(checkInDate, checkOutDate, roomType));
    }

    @PutMapping("/update/{roomId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<Response> updateRoom(
            @PathVariable Long roomId,
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            @RequestParam(value = "roomType", required = false) String roomType,
            @RequestParam(value = "roomPrice", required = false) Integer roomPrice,
            @RequestParam(value = "roomDescription", required = false) String roomDescription) {
        return ResponseEntity.ok(roomService.updateRoom(roomId, roomType, roomPrice, photo, roomDescription));
    }

    @DeleteMapping("/delete/{roomId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<Response> deleteRoom(@PathVariable Long roomId) {
        return ResponseEntity.ok(roomService.deleteRoom(roomId));
    }

    @PostMapping("/{roomId}/add-image")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<Response> addImageToRoom(
            @PathVariable Long roomId,
            @RequestParam("photo") MultipartFile photo) {
        return ResponseEntity.ok(roomService.addImageToRoom(roomId, photo));
    }

    @PostMapping("/{roomId}/add-amenity")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<Response> addAmenity(
            @PathVariable Long roomId,
            @RequestParam String name,
            @RequestParam String icon) {
        return ResponseEntity.ok(roomService.addAmenity(roomId, name, icon));
    }

    @DeleteMapping("/amenity/delete/{amenityId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<Response> deleteAmenity(@PathVariable Long amenityId) {
        return ResponseEntity.ok(roomService.deleteAmenity(amenityId));
    }

    @PostMapping("/{roomId}/meal-plan/{mealPlanId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<Response> addMealPlanToRoom(
            @PathVariable Long roomId,
            @PathVariable Long mealPlanId) {
        return ResponseEntity.ok(roomService.addMealPlanToRoom(roomId, mealPlanId));
    }

    @DeleteMapping("/{roomId}/meal-plan/{mealPlanId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<Response> removeMealPlanFromRoom(
            @PathVariable Long roomId,
            @PathVariable Long mealPlanId) {
        return ResponseEntity.ok(roomService.removeMealPlanFromRoom(roomId, mealPlanId));
    }
}