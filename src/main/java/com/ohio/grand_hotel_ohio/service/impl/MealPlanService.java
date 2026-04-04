package com.ohio.grand_hotel_ohio.service.impl;


import com.ohio.grand_hotel_ohio.domain.MealPlanType;
import com.ohio.grand_hotel_ohio.dto.MealPlanDTO;
import com.ohio.grand_hotel_ohio.dto.Response;
import com.ohio.grand_hotel_ohio.entity.MealPlan;
import com.ohio.grand_hotel_ohio.entity.Room;
import com.ohio.grand_hotel_ohio.repo.MealPlanRepository;
import com.ohio.grand_hotel_ohio.repo.RoomRepository;
import com.ohio.grand_hotel_ohio.service.interfac.IMealPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MealPlanService implements IMealPlanService {

    private final MealPlanRepository mealPlanRepo;
    private final RoomRepository roomRepository;

    public MealPlanService(MealPlanRepository mealPlanRepo, RoomRepository roomRepository) {
        this.mealPlanRepo = mealPlanRepo;
        this.roomRepository = roomRepository;
    }

    @Override
    public Response getAll() {
        List<MealPlanDTO> plans = mealPlanRepo.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
        Response response = new Response();
        response.setStatus(200);
        response.setMealPlanList(plans);
        return response;
    }

    @Override
    public Response add(String type, String name, Double pricePerNight) {
        MealPlan plan = new MealPlan();
        plan.setType(MealPlanType.valueOf(type));
        plan.setName(name);
        plan.setPricePerNight(pricePerNight);
        mealPlanRepo.save(plan);
        Response response = new Response();
        response.setStatus(200);
        response.setMessage("Étkezési csomag hozzáadva!");
        return response;
    }

    @Override
    public Response update(Long id, String name, Double pricePerNight) {
        MealPlan plan = mealPlanRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Étkezési csomag nem található"));
        if (name != null) plan.setName(name);
        if (pricePerNight != null) plan.setPricePerNight(pricePerNight);
        mealPlanRepo.save(plan);
        Response response = new Response();
        response.setStatus(200);
        response.setMessage("Étkezési csomag frissítve!");
        return response;
    }

    @Override
    public Response delete(Long id) {
        Response response = new Response();
        try {
            List<Room> rooms = roomRepository.findByMealPlansId(id);
            for (Room room : rooms) {
                room.getMealPlans().removeIf(m -> m.getId().equals(id));
                roomRepository.save(room);
            }

            mealPlanRepo.deleteById(id);
            response.setStatus(200);
            response.setMessage("Étkezési csomag törölve!");
        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage("Hiba: " + e.getMessage());
        }
        return response;
    }

    private MealPlanDTO toDTO(MealPlan p) {
        MealPlanDTO dto = new MealPlanDTO();
        dto.setId(p.getId());
        dto.setType(p.getType().name());
        dto.setName(p.getName());
        dto.setPricePerNight(p.getPricePerNight());
        return dto;
    }
}
