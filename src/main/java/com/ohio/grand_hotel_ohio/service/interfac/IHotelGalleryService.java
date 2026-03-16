package com.ohio.grand_hotel_ohio.service.interfac;

import com.ohio.grand_hotel_ohio.dto.Response;
import org.springframework.web.multipart.MultipartFile;

public interface IHotelGalleryService {
    Response getAll();
    Response getByCategory(String category);
    Response addImage(String category, String caption, MultipartFile photo);
    Response deleteImage(Long id);
}
