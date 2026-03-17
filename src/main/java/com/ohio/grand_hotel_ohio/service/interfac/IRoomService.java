package com.ohio.grand_hotel_ohio.service.interfac;

import com.ohio.grand_hotel_ohio.dto.Response;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface IRoomService {

    Response addNewRoom(MultipartFile photo, String roomType, Integer roomPrice, String description);

    List<String> getAllRoomTypes();

    Response getAllRooms();

    Response deleteRoom(Long roomId);

    Response updateRoom(Long roomId, String roomType, Integer roomPrice, MultipartFile photo, String description);

    Response getRoomById(Long roomId);

    Response getAvailableRoomsByDataAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType);

    Response getAllAvailableRooms();

    Response getAllAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate);

    Response addImageToRoom(Long roomId, MultipartFile photo);

    Response addAmenity(Long roomId, String name, String icon);

    Response deleteAmenity(Long amenityId);

    Response addMealPlanToRoom(Long roomId, Long mealPlanId);

    Response removeMealPlanFromRoom(Long roomId, Long mealPlanId);
}