package com.ohio.grand_hotel_ohio.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingDTO {

    private Long id;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int numOfAdults;
    private int numOfChildren;
    private int totalNumOfGuests;
    private String bookingConfirmationCode;
    private UserDTO users;
    private RoomDTO room;
    private List<HotelServiceDTO> selectedServices;
    private MealPlanDTO selectedMealPlan;
}
