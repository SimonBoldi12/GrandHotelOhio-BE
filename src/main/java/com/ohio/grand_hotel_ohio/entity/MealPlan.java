package com.ohio.grand_hotel_ohio.entity;

import com.ohio.grand_hotel_ohio.domain.MealPlanType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(name = "meal_plans")
@Getter
@Setter
public class MealPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private MealPlanType type;

    @Column(nullable = false)
    private String name;

    @Column(name = "price_per_night", nullable = false)
    private Double pricePerNight;


    public MealPlan() {
    }

    public MealPlan(MealPlanType type, String name, Double pricePerNight) {
        this.type = type;
        this.name = name;
        this.pricePerNight = pricePerNight;
    }
}

