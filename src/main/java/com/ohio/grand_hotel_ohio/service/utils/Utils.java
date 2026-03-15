package com.ohio.grand_hotel_ohio.service.utils;

import com.ohio.grand_hotel_ohio.dto.BookingDTO;
import com.ohio.grand_hotel_ohio.dto.RoomDTO;
import com.ohio.grand_hotel_ohio.dto.UserDTO;
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
        for(int i = 0; i<length;i++){
            int randomIndex = secureRandom.nextInt(ALPHANUMERIC_STRING.length());
            char randomChar = ALPHANUMERIC_STRING.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }

    public static UserDTO mapUserEntityToUserDTO(Users users){
        UserDTO userDTO = new UserDTO();


        userDTO.setId(users.getId());
        userDTO.setName(users.getFirstName()+ " " +users.getLastName());
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
            roomDTO.setImageUrls(
                    room.getImages().stream()
                            .map(RoomImage::getImageUrl)
                            .collect(Collectors.toList())
            );
        }
        return roomDTO;
    }

    public static BookingDTO mapBookingEntityToBookingDTO(Booking booking) {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(booking.getId());
        bookingDTO.setCheckInDate(booking.getCheckInDate());
        bookingDTO.setCheckOutDate(booking.getCheckOutDate());
        bookingDTO.setNumOfAdults(booking.getNumOfAdults());
        bookingDTO.setNumOfChildren(booking.getNumOfChildren());
        bookingDTO.setTotalNumOfGuests(booking.getTotalNumOfGuests());
        bookingDTO.setBookingConfirmationCode(booking.getBookingConfirmationCode());
        return bookingDTO;
    }


    public static RoomDTO mapRoomEntityToRoomDTOPlusBookings(Room room){
        RoomDTO roomDTO = new RoomDTO();

        roomDTO.setId(room.getId());
        roomDTO.setRoomType(room.getRoomType());
        roomDTO.setRoomPrice(room.getRoomPrice());
        roomDTO.setRoomPhotoUrl(room.getRoomPhotoUrl());
        roomDTO.setRoomDescription(room.getRoomDescription());

        if (room.getImages() != null) {
            roomDTO.setImageUrls(
                    room.getImages().stream()
                            .map(RoomImage::getImageUrl)
                            .collect(Collectors.toList())
            );
        }

        if(room.getBookings() != null){
            roomDTO.setBookings(room.getBookings().stream().map(Utils::mapBookingEntityToBookingDTO).toList());
        }
        return roomDTO;
    }



    public static UserDTO mapUserEntityToUserDTOPlusUserBookingsAndRooms(Users users){
        UserDTO userDTO = new UserDTO();


        userDTO.setId(users.getId());
        userDTO.setName(users.getFirstName()+ " " +users.getLastName());
        userDTO.setEmail(users.getEmail());
        userDTO.setPhoneNumber(users.getPhoneNumber());
        userDTO.setRole(users.getRole().name());



        if(!users.getBookings().isEmpty()){
            userDTO.setBookings(users.getBookings().stream().map(booking -> mapBookingEntityToBookingDTOPlusBookedRooms(booking,false)).toList());
        }
        return userDTO;
    }

    public static BookingDTO mapBookingEntityToBookingDTOPlusBookedRooms(Booking booking,boolean mapUser){

        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(booking.getId());
        bookingDTO.setCheckInDate(booking.getCheckInDate());
        bookingDTO.setCheckOutDate(booking.getCheckOutDate());
        bookingDTO.setNumOfAdults(booking.getNumOfAdults());
        bookingDTO.setNumOfChildren(booking.getNumOfChildren());
        bookingDTO.setTotalNumOfGuests(booking.getTotalNumOfGuests());
        bookingDTO.setBookingConfirmationCode(booking.getBookingConfirmationCode());

        if(mapUser){
            bookingDTO.setUsers(Utils.mapUserEntityToUserDTO(booking.getUsers()));
        }
        if(booking.getRoom() != null){
            RoomDTO roomDTO = new RoomDTO();

            roomDTO.setId(booking.getRoom().getId());
            roomDTO.setRoomType(booking.getRoom().getRoomType());
            roomDTO.setRoomPrice(booking.getRoom().getRoomPrice());
            roomDTO.setRoomPhotoUrl(booking.getRoom().getRoomPhotoUrl());
            roomDTO.setRoomDescription(booking.getRoom().getRoomDescription());
            bookingDTO.setRoom(roomDTO);
        }
        return bookingDTO;

    }

    public static List<UserDTO>mapUserListEntityToUserListDTO(List<Users> userList){
        return userList.stream().map(Utils::mapUserEntityToUserDTO).toList();
    }

    public static List<RoomDTO>mapRoomListEntityToRoomListDTO(List<Room> roomList){
        return roomList.stream().map(Utils::mapRoomEntityToRoomDTO).toList();
    }

    public static List<BookingDTO>mapBookingListEntityToBookingListDTO(List<Booking> bookingList){
        return bookingList.stream().map(Utils::mapBookingEntityToBookingDTO).toList();
    }


}
