package com.ohio.grand_hotel_ohio.service.impl;

import com.ohio.grand_hotel_ohio.dto.BookingDTO;
import com.ohio.grand_hotel_ohio.dto.Response;
import com.ohio.grand_hotel_ohio.entity.Booking;
import com.ohio.grand_hotel_ohio.entity.HotelService;
import com.ohio.grand_hotel_ohio.entity.MealPlan;
import com.ohio.grand_hotel_ohio.entity.Room;
import com.ohio.grand_hotel_ohio.entity.Users;
import com.ohio.grand_hotel_ohio.exception.OurException;
import com.ohio.grand_hotel_ohio.repo.BookingRepository;
import com.ohio.grand_hotel_ohio.repo.HotelServiceRepository;
import com.ohio.grand_hotel_ohio.repo.MealPlanRepository;
import com.ohio.grand_hotel_ohio.repo.RoomRepository;
import com.ohio.grand_hotel_ohio.repo.UserRepository;
import com.ohio.grand_hotel_ohio.service.interfac.IBookingService;
import com.ohio.grand_hotel_ohio.service.interfac.IEmailService;
import com.ohio.grand_hotel_ohio.service.interfac.IRoomService;
import com.ohio.grand_hotel_ohio.service.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookingService implements IBookingService {

    private final BookingRepository bookingRepository;
    private final IRoomService roomService;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final IEmailService emailService;
    private final HotelServiceRepository hotelServiceRepository;
    private final MealPlanRepository mealPlanRepository;

    public BookingService(BookingRepository bookingRepository, IRoomService roomService, RoomRepository roomRepository, UserRepository userRepository, IEmailService emailService, HotelServiceRepository hotelServiceRepository, MealPlanRepository mealPlanRepository) {
        this.bookingRepository = bookingRepository;
        this.roomService = roomService;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.hotelServiceRepository = hotelServiceRepository;
        this.mealPlanRepository = mealPlanRepository;
    }

    @Override
    public Response saveBooking(Long roomId, Long userId, Booking bookingRequest, Long mealPlanId) {

        Response response = new Response();

        try {
            if (bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
                throw new IllegalArgumentException("A kijelentkezési dátum nem lehet korábbi, mint a bejelentkezési dátum.");
            }
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new OurException("Nem található a szoba"));
            Users user = userRepository.findById(userId).orElseThrow(() -> new OurException("Nem található felhasználó"));

            List<Booking> existingBookings = room.getBookings();

            if (!roomIsAvailable(bookingRequest, existingBookings)) {
                throw new OurException("A szoba nem elérhető a kiválasztott dátumtartományban.");
            }

            bookingRequest.setRoom(room);
            bookingRequest.setUsers(user);

            if (mealPlanId != null) {
                MealPlan mealPlan = mealPlanRepository.findById(mealPlanId).orElse(null);
                bookingRequest.setSelectedMealPlan(mealPlan);
            }

            String bookingConfirmationCode = Utils.generateRandomConfirmationCode(10);
            bookingRequest.setBookingConfirmationCode(bookingConfirmationCode);
            bookingRepository.save(bookingRequest);

            try {
                emailService.sendBookingConfirmation(bookingRequest);
            } catch (Exception emailException) {
                System.err.println("Figyelem: A foglalás sikeres, de a visszaigazoló emailt nem sikerült elküldeni. Hiba: " + emailException.getMessage());
            }

            response.setStatus(200);
            response.setMessage("successful");
            response.setBookingConfirmationCode(bookingConfirmationCode);

        } catch (OurException e) {
            response.setStatus(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage("Hiba a foglalás mentése közben: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response findBookingByConfirmationCode(String confirmationCode) {
        Response response = new Response();
        try {
            Booking booking = bookingRepository.findByBookingConfirmationCode(confirmationCode)
                    .orElseThrow(() -> new OurException("A foglalás nem található"));
            BookingDTO bookingDTO = Utils.mapBookingEntityToBookingDTOPlusBookedRooms(booking, true);
            response.setStatus(200);
            response.setMessage("sikeres");
            response.setBooking(bookingDTO);
        } catch (OurException e) {
            response.setStatus(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage("Hiba a foglalás keresése közben: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllBookings() {
        Response response = new Response();
        try {
            List<Booking> bookingList = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<BookingDTO> bookingDTOList = bookingList.stream()
                    .map(booking -> Utils.mapBookingEntityToBookingDTOPlusBookedRooms(booking, true))
                    .toList();
            response.setStatus(200);
            response.setMessage("sikeres");
            response.setBookings(bookingDTOList);
        } catch (OurException e) {
            response.setStatus(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage("Hiba az összes szoba visszaadásakor: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response cancelBooking(Long bookingId) {
        Response response = new Response();
        try {
            bookingRepository.findById(bookingId).orElseThrow(() -> new OurException("A foglalás nem létezik"));
            bookingRepository.deleteById(bookingId);
            response.setStatus(200);
            response.setMessage("sikeres");
        } catch (OurException e) {
            response.setStatus(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage("Hiba a szoba lemondásakor: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response addServiceToBooking(Long bookingId, Long serviceId) {
        Response response = new Response();
        try {
            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new OurException("Foglalás nem található"));
            HotelService service = hotelServiceRepository.findById(serviceId)
                    .orElseThrow(() -> new OurException("Szolgáltatás nem található"));

            booking.getSelectedServices().add(service);
            bookingRepository.save(booking);

            response.setStatus(200);
            response.setMessage("Szolgáltatás hozzáadva!");
        } catch (OurException e) {
            response.setStatus(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage("Hiba: " + e.getMessage());
        }
        return response;
    }

    private boolean roomIsAvailable(Booking bookingRequest, List<Booking> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                                || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                                || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))
                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
                );
    }
}