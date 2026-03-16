package com.ohio.grand_hotel_ohio.controller;

import com.ohio.grand_hotel_ohio.dto.Response;
import com.ohio.grand_hotel_ohio.service.interfac.IHotelServiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/services")
public class HotelServiceController {

    private final IHotelServiceService hotelServiceService;

    public HotelServiceController(IHotelServiceService hotelServiceService) {
        this.hotelServiceService = hotelServiceService;
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllServices() {
        return ResponseEntity.ok(hotelServiceService.getAllServices());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Response> getByCategory(@PathVariable String category) {
        return ResponseEntity.ok(hotelServiceService.getByCategory(category));
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> addService(
            @RequestParam String category,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam Double price,
            @RequestParam(required = false) MultipartFile photo) {
        return ResponseEntity.ok(hotelServiceService.addService(category, name, description, price, photo));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> deleteService(@PathVariable Long id) {
        return ResponseEntity.ok(hotelServiceService.deleteService(id));
    }
}