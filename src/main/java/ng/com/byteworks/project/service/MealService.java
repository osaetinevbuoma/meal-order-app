package ng.com.byteworks.project.service;

import ng.com.byteworks.project.db.entity.Meal;
import ng.com.byteworks.project.db.repository.MealRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
            // carts and orders are not needed.
            meal.setCarts(null);
            meal.setOrders(null);
        });
        return meals;
    }

    public Map<String, Object> getMeal(int id) {
        Optional<Meal> meal = mealRepository.findById(id);
        return meal.map(utilService::generateMealMap).orElse(null);
    }
}
