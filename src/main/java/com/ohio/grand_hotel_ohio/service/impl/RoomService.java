package com.ohio.grand_hotel_ohio.service.impl;

import com.ohio.grand_hotel_ohio.dto.Response;
import com.ohio.grand_hotel_ohio.dto.RoomDTO;
import com.ohio.grand_hotel_ohio.entity.MealPlan;
import com.ohio.grand_hotel_ohio.entity.Room;
import com.ohio.grand_hotel_ohio.entity.RoomAmenity;
import com.ohio.grand_hotel_ohio.entity.RoomImage;
import com.ohio.grand_hotel_ohio.exception.OurException;
import com.ohio.grand_hotel_ohio.repo.BookingRepository;
import com.ohio.grand_hotel_ohio.repo.MealPlanRepository;
import com.ohio.grand_hotel_ohio.repo.RoomAmenityRepository;
import com.ohio.grand_hotel_ohio.repo.RoomRepository;
import com.ohio.grand_hotel_ohio.service.AwsS3Service;
import com.ohio.grand_hotel_ohio.service.interfac.IRoomService;
import com.ohio.grand_hotel_ohio.service.utils.Utils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService implements IRoomService {

    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final AwsS3Service awsS3Service;
    private final RoomAmenityRepository amenityRepository;
    private final MealPlanRepository mealPlanRepository;

    public RoomService(RoomRepository roomRepository, BookingRepository bookingRepository, AwsS3Service awsS3Service, RoomAmenityRepository amenityRepository, MealPlanRepository mealPlanRepository) {
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
        this.awsS3Service = awsS3Service;
        this.amenityRepository = amenityRepository;
        this.mealPlanRepository = mealPlanRepository;
    }

    @Override
    public Response addNewRoom(MultipartFile photo, String roomType, Integer roomPrice, String description) {
        Response response = new Response();
        try {
            String imageUrl = awsS3Service.saveImageToS3(photo);
            Room room = new Room();
            room.setRoomPhotoUrl(imageUrl);
            room.setRoomType(roomType);
            room.setRoomPrice(roomPrice);
            room.setRoomDescription(description);
            Room savedRoom = roomRepository.save(room);
            RoomDTO roomDto = Utils.mapRoomEntityToRoomDTO(savedRoom);

            response.setStatus(200);
            response.setMessage("Successful");
            response.setRoom(roomDto);
        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage("Error saving a room: " + e.getMessage());
        }
        return response;
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }

    @Override
    public Response getAllRooms() {
        Response response = new Response();
        try {
            List<Room> roomList = roomRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDTO(roomList);

            response.setStatus(200);
            response.setMessage("Successful");
            response.setRoomList(roomDTOList);
        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage("Error fetching rooms: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteRoom(Long roomId) {
        Response response = new Response();
        try {
            roomRepository.findById(roomId).orElseThrow(() -> new OurException("Room Not Found"));
            roomRepository.deleteById(roomId);

            response.setStatus(200);
            response.setMessage("Successful");
        } catch (OurException e) {
            response.setStatus(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage("Error deleting room: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateRoom(Long roomId, String roomType, Integer roomPrice, MultipartFile photo, String description) {
        Response response = new Response();
        try {
            String imageUrl = null;
            if (photo != null && !photo.isEmpty()) {
                imageUrl = awsS3Service.saveImageToS3(photo);
            }

            Room room = roomRepository.findById(roomId).orElseThrow(() -> new OurException("Room Not Found"));
            if (roomType != null) room.setRoomType(roomType);
            if (roomPrice != null) room.setRoomPrice(roomPrice);
            if (description != null) room.setRoomDescription(description);
            if (imageUrl != null) room.setRoomPhotoUrl(imageUrl);

            Room updatedRoom = roomRepository.save(room);
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTO(updatedRoom);

            response.setStatus(200);
            response.setMessage("Successful");
            response.setRoom(roomDTO);
        } catch (OurException e) {
            response.setStatus(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage("Error updating room: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getRoomById(Long roomId) {
        Response response = new Response();
        try {
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new OurException("Room Not Found"));
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTOPlusBookings(room);

            response.setStatus(200);
            response.setMessage("Successful");
            response.setRoom(roomDTO);
        } catch (OurException e) {
            response.setStatus(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage("Error fetching room: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAvailableRoomsByDataAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        Response response = new Response();
        try {
            List<Room> availableRooms = roomRepository.findAvailableRoomsByDatesAndTypes(checkInDate, checkOutDate, roomType);
            List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDTO(availableRooms);

            response.setStatus(200);
            response.setMessage("Successful");
            response.setRoomList(roomDTOList);
        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage("Error fetching available rooms: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllAvailableRooms() {
        Response response = new Response();
        try {
            List<Room> roomList = roomRepository.getAllAvailableRooms();
            List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDTO(roomList);

            response.setStatus(200);
            response.setMessage("Successful");
            response.setRoomList(roomDTOList);
        } catch (OurException e) {
            response.setStatus(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage("Error fetching available rooms: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate) {
        Response response = new Response();
        try {
            List<Room> availableRooms = roomRepository.findAll().stream()
                    .filter(room -> room.getBookings().stream().noneMatch(booking ->
                            !booking.getCheckInDate().isAfter(checkOutDate) &&
                                    !booking.getCheckOutDate().isBefore(checkInDate)
                    ))
                    .collect(Collectors.toList());

            List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDTO(availableRooms);

            response.setStatus(200);
            response.setMessage("Successful");
            response.setRoomList(roomDTOList);
        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage("Error fetching available rooms: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response addImageToRoom(Long roomId, MultipartFile photo) {
        Response response = new Response();
        try {
            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new OurException("Room Not Found"));
            String imageUrl = awsS3Service.saveImageToS3(photo);
            RoomImage roomImage = new RoomImage();
            roomImage.setImageUrl(imageUrl);
            roomImage.setRoom(room);
            room.getImages().add(roomImage);
            roomRepository.save(room);

            response.setStatus(200);
            response.setMessage("Successful");
        } catch (OurException e) {
            response.setStatus(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage("Error adding image: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response addAmenity(Long roomId, String name, String icon) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Szoba nem található"));
        RoomAmenity amenity = new RoomAmenity();
        amenity.setName(name);
        amenity.setIcon(icon);
        amenity.setRoom(room);
        amenityRepository.save(amenity);
        Response response = new Response();
        response.setStatus(200);
        response.setMessage("Felszereltség hozzáadva!");
        return response;
    }

    @Override
    public Response deleteAmenity(Long amenityId) {
        amenityRepository.deleteById(amenityId);
        Response response = new Response();
        response.setStatus(200);
        response.setMessage("Felszereltség törölve!");
        return response;
    }

    @Override
    public Response setMealPlan(Long roomId, Long mealPlanId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Szoba nem található"));
        MealPlan mealPlan = mealPlanRepository.findById(mealPlanId)
                .orElseThrow(() -> new RuntimeException("Étkezési csomag nem található"));
        room.setMealPlan(mealPlan);
        roomRepository.save(room);
        Response response = new Response();
        response.setStatus(200);
        response.setMessage("Étkezési csomag beállítva!");
        return response;
    }
}