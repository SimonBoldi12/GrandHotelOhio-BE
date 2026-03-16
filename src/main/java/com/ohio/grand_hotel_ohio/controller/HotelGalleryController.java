package com.ohio.grand_hotel_ohio.controller;

import com.ohio.grand_hotel_ohio.dto.Response;
import com.ohio.grand_hotel_ohio.service.interfac.IHotelGalleryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/gallery")
public class HotelGalleryController {

    private final IHotelGalleryService galleryService;

    public HotelGalleryController(IHotelGalleryService galleryService) {
        this.galleryService = galleryService;
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAll() {
        return ResponseEntity.ok(galleryService.getAll());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Response> getByCategory(@PathVariable String category) {
        return ResponseEntity.ok(galleryService.getByCategory(category));
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> addImage(
            @RequestParam String category,
            @RequestParam(required = false) String caption,
            @RequestParam MultipartFile photo) {
        return ResponseEntity.ok(galleryService.addImage(category, caption, photo));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> deleteImage(@PathVariable Long id) {
        return ResponseEntity.ok(galleryService.deleteImage(id));
    }
}