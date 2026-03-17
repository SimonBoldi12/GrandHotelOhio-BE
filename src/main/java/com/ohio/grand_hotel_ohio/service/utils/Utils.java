package com.ohio.grand_hotel_ohio.service.utils;

import com.ohio.grand_hotel_ohio.dto.*;
import com.ohio.grand_hotel_ohio.entity.Booking;
import com.ohio.grand_hotel_ohio.entity.Room;
import com.ohio.grand_hotel_ohio.entity.RoomImage;
import com.ohio.grand_hotel_ohio.entity.Users;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {
    private static final String ALPHANUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateRandomConfirmationCode(int length){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < length; i++){
            int randomIndex = secureRandom.nextInt(ALPHANUMERIC_STRING.length());
            char randomChar = ALPHANUMERIC_STRING.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }

    public static UserDTO mapUserEntityToUserDTO(Users users){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(users.getId());
        userDTO.setName(users.getFirstName() + " " + users.getLastName());
        userDTO.setEmail(users.getEmail());
        userDTO.setPhoneNumber(users.getPhoneNumber());
        userDTO.setRole(users.getRole().name());
        return userDTO;
    }

    public static RoomDTO mapRoomEntityToRoomDTO(Room room){
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(room.getId());
        roomDTO.setRoomType(room.getRoomType());
        roomDTO.setRoomPrice(room.getRoomPrice());
        roomDTO.setRoomPhotoUrl(room.getRoomPhotoUrl());
        roomDTO.setRoomDescription(room.getRoomDescription());

        if (room.getImages() != null) {
            roomDTO.setImageUrls(room.getImages().stream().map(RoomImage::getImageUrl).collect(Collectors.toList()));
        }

        if (room.getAmenities() != null && !room.getAmenities().isEmpty()) {
            roomDTO.setAmenities(room.getAmenities().stream().map(a -> {
                RoomAmenityDTO dto = new RoomAmenityDTO();
                dto.setId(a.getId());
                dto.setName(a.getName());
                dto.setIcon(a.getIcon());
                return dto;
            }).collect(Collectors.toList()));
        }

        if (room.getMealPlans() != null && !room.getMealPlans().isEmpty()) {
            roomDTO.setMealPlans(room.getMealPlans().stream().map(m -> {
                MealPlanDTO dto = new MealPlanDTO();
                dto.setId(m.getId());
                dto.setType(m.getType().name());
                dto.setName(m.getName());
                dto.setPricePerNight(m.getPricePerNight());
                return dto;
            }).collect(Collectors.toList()));
        }
        return roomDTO;
    }

    // JAVÍTVA: Most már ez is a részletes mappelőt használja, hogy sehol ne legyen undefined az adat
    public static BookingDTO mapBookingEntityToBookingDTO(Booking booking) {
        return mapBookingEntityToBookingDTOPlusBookedRooms(booking, true);
    }

    public static RoomDTO mapRoomEntityToRoomDTOPlusBookings(Room room){
        RoomDTO roomDTO = mapRoomEntityToRoomDTO(room);
        if (room.getBookings() != null) {
            roomDTO.setBookings(room.getBookings().stream().map(Utils::mapBookingEntityToBookingDTO).toList());
        }
        return roomDTO;
    }

    public static UserDTO mapUserEntityToUserDTOPlusUserBookingsAndRooms(Users users){
        UserDTO userDTO = mapUserEntityToUserDTO(users);
        if(users.getBookings() != null && !users.getBookings().isEmpty()){
            userDTO.setBookings(users.getBookings().stream().map(booking -> mapBookingEntityToBookingDTOPlusBookedRooms(booking, false)).toList());
        }
        return userDTO;
    }

    public static BookingDTO mapBookingEntityToBookingDTOPlusBookedRooms(Booking booking, boolean mapUser) {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(booking.getId());
        bookingDTO.setCheckInDate(booking.getCheckInDate());
        bookingDTO.setCheckOutDate(booking.getCheckOutDate());
        bookingDTO.setNumOfAdults(booking.getNumOfAdults());
        bookingDTO.setNumOfChildren(booking.getNumOfChildren());
        bookingDTO.setTotalNumOfGuests(booking.getTotalNumOfGuests());
        bookingDTO.setBookingConfirmationCode(booking.getBookingConfirmationCode());

        if (mapUser && booking.getUsers() != null) {
            bookingDTO.setUsers(Utils.mapUserEntityToUserDTO(booking.getUsers()));
        }

        if (booking.getRoom() != null) {
            bookingDTO.setRoom(mapRoomEntityToRoomDTO(booking.getRoom()));
        }

        if (booking.getSelectedServices() != null && !booking.getSelectedServices().isEmpty()) {
            bookingDTO.setSelectedServices(booking.getSelectedServices().stream().map(s -> {
                HotelServiceDTO dto = new HotelServiceDTO();
                dto.setId(s.getId());
                dto.setCategory(s.getCategory());
                dto.setName(s.getName());
                dto.setDescription(s.getDescription());
                dto.setPrice(s.getPrice());
                dto.setPhotoUrl(s.getPhotoUrl());
                dto.setAvailable(s.getAvailable());
                return dto;
            }).collect(Collectors.toList()));
        }

        if (booking.getSelectedMealPlan() != null) {
            MealPlanDTO mpDTO = new MealPlanDTO();
            mpDTO.setId(booking.getSelectedMealPlan().getId());
            mpDTO.setType(booking.getSelectedMealPlan().getType().name());
            mpDTO.setName(booking.getSelectedMealPlan().getName());
            mpDTO.setPricePerNight(booking.getSelectedMealPlan().getPricePerNight());
            bookingDTO.setSelectedMealPlan(mpDTO);
        }

        return bookingDTO;
    }

    public static List<UserDTO> mapUserListEntityToUserListDTO(List<Users> userList){
        return userList.stream().map(Utils::mapUserEntityToUserDTO).toList();
    }

    public static List<RoomDTO> mapRoomListEntityToRoomListDTO(List<Room> roomList){
        return roomList.stream().map(Utils::mapRoomEntityToRoomDTO).toList();
    }

    public static List<BookingDTO> mapBookingListEntityToBookingListDTO(List<Booking> bookingList){
        return bookingList.stream().map(b -> mapBookingEntityToBookingDTOPlusBookedRooms(b, true)).toList();
    }
}