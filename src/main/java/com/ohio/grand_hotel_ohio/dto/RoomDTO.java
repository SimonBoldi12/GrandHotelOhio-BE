package com.ohio.grand_hotel_ohio.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;


import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomDTO {

    private Long id;
    private String roomType;
    private Integer roomPrice;
    private String roomPhotoUrl;
    private String roomDescription;
    private List<BookingDTO> bookings;
    private List<String> imageUrls;
    private List<RoomAmenityDTO> amenities;
    private MealPlanDTO mealPlan;



    public RoomDTO(Long id, String roomType, Integer roomPrice, String roomPhotoUrl, String roomDescription) {
        this.id = id;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
        this.roomPhotoUrl = roomPhotoUrl;
        this.roomDescription = roomDescription;
    }

    public RoomDTO() {

    }
}
