package com.ohio.grand_hotel_ohio.service.interfac;

import com.ohio.grand_hotel_ohio.entity.Booking;

public interface IEmailService {
    void sendBookingConfirmation(Booking booking);
}
