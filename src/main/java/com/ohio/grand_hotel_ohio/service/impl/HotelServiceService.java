package com.ohio.grand_hotel_ohio.service.impl;

import com.ohio.grand_hotel_ohio.dto.HotelServiceDTO;
import com.ohio.grand_hotel_ohio.dto.Response;
import com.ohio.grand_hotel_ohio.entity.HotelService;
import com.ohio.grand_hotel_ohio.repo.HotelServiceRepository;
import com.ohio.grand_hotel_ohio.service.AwsS3Service;
import com.ohio.grand_hotel_ohio.service.interfac.IHotelServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelServiceService implements IHotelServiceService {

    @Autowired
    private HotelServiceRepository serviceRepo;

    @Autowired
    private AwsS3Service s3Service;

    @Override
    public Response getAllServices() {
        List<HotelServiceDTO> services = serviceRepo.findByAvailableTrue()
                .stream().map(this::toDTO).collect(Collectors.toList());
        Response response = new Response();
        response.setStatus(200);
        response.setServiceList(services);
        return response;
    }

    @Override
    public Response getByCategory(String category) {
        List<HotelServiceDTO> services = serviceRepo.findByCategoryAndAvailableTrue(category)
                .stream().map(this::toDTO).collect(Collectors.toList());
        Response response = new Response();
        response.setStatus(200);
        response.setServiceList(services);
        return response;
    }

    @Override
    public Response addService(String category, String name, String description, Double price, MultipartFile photo) {
        HotelService service = new HotelService();
        service.setCategory(category);
        service.setName(name);
        service.setDescription(description);
        service.setPrice(price);
        service.setAvailable(true);
        if (photo != null && !photo.isEmpty()) {
            service.setPhotoUrl(s3Service.saveImageToS3(photo));
        }
        serviceRepo.save(service);
        Response response = new Response();
        response.setStatus(200);
        response.setMessage("Szolgáltatás sikeresen hozzáadva!");
        return response;
    }

    @Override
    public Response updateService(Long id, String name, String description, Double price, Boolean available) {
        HotelService service = serviceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Szolgáltatás nem található"));
        if (name != null) service.setName(name);
        if (description != null) service.setDescription(description);
        if (price != null) service.setPrice(price);
        if (available != null) service.setAvailable(available);
        serviceRepo.save(service);
        Response response = new Response();
        response.setStatus(200);
        response.setMessage("Szolgáltatás frissítve!");
        return response;
    }

    @Override
    public Response deleteService(Long id) {
        serviceRepo.deleteById(id);
        Response response = new Response();
        response.setStatus(200);
        response.setMessage("Szolgáltatás törölve!");
        return response;
    }

    private HotelServiceDTO toDTO(HotelService s) {
        HotelServiceDTO dto = new HotelServiceDTO();
        dto.setId(s.getId());
        dto.setCategory(s.getCategory());
        dto.setName(s.getName());
        dto.setDescription(s.getDescription());
        dto.setPrice(s.getPrice());
        dto.setPhotoUrl(s.getPhotoUrl());
        dto.setAvailable(s.getAvailable());
        return dto;
    }
}