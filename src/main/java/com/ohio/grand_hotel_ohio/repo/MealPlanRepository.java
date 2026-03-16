package com.ohio.grand_hotel_ohio.repo;

import com.ohio.grand_hotel_ohio.domain.MealPlanType;
import com.ohio.grand_hotel_ohio.entity.MealPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MealPlanRepository extends JpaRepository<MealPlan, Long> {
    Optional<MealPlan> findByType(MealPlanType type);

}

