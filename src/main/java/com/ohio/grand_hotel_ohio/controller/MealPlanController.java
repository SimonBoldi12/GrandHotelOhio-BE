package com.ohio.grand_hotel_ohio.controller;

import com.ohio.grand_hotel_ohio.dto.Response;
import com.ohio.grand_hotel_ohio.service.interfac.IMealPlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/meal-plans")
public class MealPlanController {

    private final IMealPlanService mealPlanService;

    public MealPlanController(IMealPlanService mealPlanService) {
        this.mealPlanService = mealPlanService;
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAll() {
        return ResponseEntity.ok(mealPlanService.getAll());
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> add(
            @RequestParam String type,
            @RequestParam String name,
            @RequestParam Double pricePerNight) {
        return ResponseEntity.ok(mealPlanService.add(type, name, pricePerNight));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> update(
            @PathVariable Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double pricePerNight) {
        return ResponseEntity.ok(mealPlanService.update(id, name, pricePerNight));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> delete(@PathVariable Long id) {
        return ResponseEntity.ok(mealPlanService.delete(id));
    }
}