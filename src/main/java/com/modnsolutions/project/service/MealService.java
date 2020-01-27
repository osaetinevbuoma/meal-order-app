package com.modnsolutions.project.service;

import com.modnsolutions.project.db.entity.Meal;
import com.modnsolutions.project.db.repository.MealRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@Secured("ROLE_DEVELOPER")
public class MealService {
    private final MealRepository mealRepository;
    private final UtilService utilService;

    public MealService(MealRepository mealRepository, UtilService utilService) {
        this.mealRepository = mealRepository;
        this.utilService = utilService;
    }

    public Page<Meal> listMeals(Pageable pageable) {
        Page<Meal> meals = mealRepository.findAllByIsAvailableTrue(pageable);
        meals.get().forEach(meal -> {
            // carts and ordered meals are not needed.
            meal.setCarts(null);
            meal.setOrderedMeals(null);
        });
        return meals;
    }

    public Map<String, Object> getMeal(int id) {
        Optional<Meal> meal = mealRepository.findById(id);
        return meal.map(utilService::generateMealMap).orElse(null);
    }
}
