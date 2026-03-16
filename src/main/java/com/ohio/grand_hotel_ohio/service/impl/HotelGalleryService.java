package com.ohio.grand_hotel_ohio.service.impl;

import com.ohio.grand_hotel_ohio.dto.HotelGalleryDTO;
import com.ohio.grand_hotel_ohio.dto.Response;
import com.ohio.grand_hotel_ohio.entity.HotelGallery;
import com.ohio.grand_hotel_ohio.repo.HotelGalleryRepository;
import com.ohio.grand_hotel_ohio.service.AwsS3Service;
import com.ohio.grand_hotel_ohio.service.interfac.IHotelGalleryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelGalleryService implements IHotelGalleryService {

    @Autowired
    private HotelGalleryRepository galleryRepo;

    @Autowired
    private AwsS3Service s3Service;

    @Override
    public Response getAll() {
        List<HotelGalleryDTO> items = galleryRepo.findByOrderByCategoryAsc()
                .stream().map(this::toDTO).collect(Collectors.toList());
        Response response = new Response();
        response.setStatus(200);
        response.setGalleryList(items);
        return response;
    }

    @Override
    public Response getByCategory(String category) {
        List<HotelGalleryDTO> items = galleryRepo.findByCategory(category)
                .stream().map(this::toDTO).collect(Collectors.toList());
        Response response = new Response();
        response.setStatus(200);
        response.setGalleryList(items);
        return response;
    }

    @Override
    public Response addImage(String category, String caption, MultipartFile photo) {
        HotelGallery item = new HotelGallery();
        item.setCategory(category);
        item.setCaption(caption != null ? caption : "");
        item.setImageUrl(s3Service.saveImageToS3(photo));
        galleryRepo.save(item);
        Response response = new Response();
        response.setStatus(200);
        response.setMessage("Kép sikeresen feltöltve!");
        return response;
    }

    @Override
    public Response deleteImage(Long id) {
        galleryRepo.deleteById(id);
        Response response = new Response();
        response.setStatus(200);
        response.setMessage("Kép törölve!");
        return response;
    }

    private HotelGalleryDTO toDTO(HotelGallery g) {
        HotelGalleryDTO dto = new HotelGalleryDTO();
        dto.setId(g.getId());
        dto.setCategory(g.getCategory());
        dto.setImageUrl(g.getImageUrl());
        dto.setCaption(g.getCaption());
        return dto;
    }
}