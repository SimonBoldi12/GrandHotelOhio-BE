package com.ohio.grand_hotel_ohio.controller;

import com.ohio.grand_hotel_ohio.dto.Response;
import com.ohio.grand_hotel_ohio.entity.Booking;
import com.ohio.grand_hotel_ohio.service.interfac.IBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")

public class BookingController {


    private final IBookingService bookinService;

    public BookingController(IBookingService bookinService) {
        this.bookinService = bookinService;
    }

    @PostMapping("/book-room/{roomId}/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('STAFF')")
    public ResponseEntity<Response> saveBookings(
            @PathVariable Long roomId,
            @PathVariable Long userId,
            @RequestBody Booking bookingRequest,
            @RequestParam(required = false) Long mealPlanId) {

        Response response = bookinService.saveBooking(roomId, userId, bookingRequest, mealPlanId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<Response> getAllBookings(){
        Response response = bookinService.getAllBookings();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/get-by-confirmation-code/{confirmationCode}")
    public ResponseEntity<Response> getBookingByConfirmationCode(@PathVariable String confirmationCode){
        Response response = bookinService.findBookingByConfirmationCode(confirmationCode);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/cancel/{bookingId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('STAFF')")
    public ResponseEntity<Response> cancelBooking(@PathVariable Long bookingId){
        Response response = bookinService.cancelBooking(bookingId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/{bookingId}/add-service/{serviceId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<Response> addServiceToBooking(
            @PathVariable Long bookingId,
            @PathVariable Long serviceId) {
        return ResponseEntity.ok(bookinService.addServiceToBooking(bookingId, serviceId));
    }

}
