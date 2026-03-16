package com.ohio.grand_hotel_ohio.service.interfac;

import com.ohio.grand_hotel_ohio.dto.Response;

public interface IMealPlanService {
    Response getAll();
    Response add(String type, String name, Double pricePerNight);
    Response update(Long id, String name, Double pricePerNight);
    Response delete(Long id);
}
