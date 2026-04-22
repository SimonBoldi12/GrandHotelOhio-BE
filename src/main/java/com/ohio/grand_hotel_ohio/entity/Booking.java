package com.ohio.grand_hotel_ohio.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "booking")
@Getter
@Setter
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "A bejelentkezés dátumát kötelező megadni!")
    @Column(name = "check_in_date",nullable = false)
    private LocalDate checkInDate;

    @Future(message = "A kölcsönzés visszaadási dátumának a jövőben kell lennie!")
    @Column(name = "check_out_date",nullable = false)
    private LocalDate checkOutDate;

    @Min(value = 1, message = "A felnőttek száma nem lehet kevesebb 1-nél!")
    @Column(name = "num_of_adults",nullable = false)
    private int numOfAdults;

    @Min(value = 0, message = "A gyermekek száma nem lehet 0-nál kevesebb!")
    @Column(name = "num_of_children",nullable = false)
    private int numOfChildren;

    @Column(name = "total_num_of_guests")
    private int totalNumOfGuests;


    @Column(name = "booking_confirmation_code",nullable = false,unique = true)
    private String bookingConfirmationCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",nullable = false)
    private Users users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id",nullable = false)
    private Room room;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "booking_services",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private List<HotelService> selectedServices = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "selected_meal_plan_id")
    private MealPlan selectedMealPlan;

    // összes vendég számítása
    public void calculateTotalNumOfGuests() {
        this.totalNumOfGuests = this.numOfAdults + this.numOfChildren;
    }

    //setter

    public void setNumOfAdults(int numOfAdults) {
        this.numOfAdults = numOfAdults;
        calculateTotalNumOfGuests();
    }

    public void setNumOfChildren(int numOfChildren) {
        this.numOfChildren = numOfChildren;
        calculateTotalNumOfGuests();
    }


    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                ", numOfAdults=" + numOfAdults +
                ", numOfChildren=" + numOfChildren +
                ", totalNumOfGuests=" + totalNumOfGuests +
                ", bookingConfirmationCode='" + bookingConfirmationCode + '\'' +
                '}';
    }
}