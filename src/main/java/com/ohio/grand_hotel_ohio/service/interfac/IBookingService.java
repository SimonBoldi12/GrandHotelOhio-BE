package com.ohio.grand_hotel_ohio.service.interfac;

import com.ohio.grand_hotel_ohio.dto.Response;
import com.ohio.grand_hotel_ohio.entity.Booking;

public interface IBookingService {

    Response saveBooking(Long roomId, Long userId, Booking booking, Long mealPlanId);
    Response findBookingByConfirmationCode(String confirmationCode);
    Response getAllBookings();
    Response cancelBooking(Long bookingId);
    Response addServiceToBooking(Long bookingId, Long serviceId);
}
