package com.ohio.grand_hotel_ohio.service.impl;

import com.ohio.grand_hotel_ohio.entity.Booking;
import com.ohio.grand_hotel_ohio.service.interfac.IEmailService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService implements IEmailService {

    @Autowired
    private JavaMailSender javaMailSender;


    @Autowired
    private TemplateEngine templateEngine;

    @Async
    @Override
    public void sendBookingConfirmation(Booking booking) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(booking.getUsers().getEmail());
            helper.setSubject("Sikeres foglalás - Grand Hotel Ohio 🏨");


            Context context = new Context();
            context.setVariable("firstName", booking.getUsers().getFirstName());
            context.setVariable("roomType", booking.getRoom().getRoomType());
            context.setVariable("checkIn", booking.getCheckInDate().toString());
            context.setVariable("checkOut", booking.getCheckOutDate().toString());
            context.setVariable("guests", booking.getTotalNumOfGuests());
            context.setVariable("confirmationCode", booking.getBookingConfirmationCode());


            String htmlContent = templateEngine.process("bookingconfirmation", context);


            helper.setText(htmlContent, true);


            javaMailSender.send(message);

        } catch (Exception e) {
            System.err.println("Hiba a HTML email küldésekor: " + e.getMessage());
        }
    }
}