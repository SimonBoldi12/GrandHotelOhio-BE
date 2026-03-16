package com.ohio.grand_hotel_ohio.service.interfac;

import com.ohio.grand_hotel_ohio.dto.Response;
import org.springframework.web.multipart.MultipartFile;

public interface IHotelServiceService {
    Response getAllServices();
    Response getByCategory(String category);
    Response addService(String category, String name, String description, Double price, MultipartFile photo);
    Response updateService(Long id, String name, String description, Double price, Boolean available);
    Response deleteService(Long id);
}
