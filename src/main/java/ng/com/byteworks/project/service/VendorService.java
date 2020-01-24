package ng.com.byteworks.project.service;

import ng.com.byteworks.project.db.entity.Meal;
import ng.com.byteworks.project.db.repository.MealRepository;
import ng.com.byteworks.project.db.repository.OrderRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Secured("ROLE_VENDOR")
public class VendorService {
    private final MealRepository mealRepository;
    private final OrderRepository orderRepository;
    private final UtilService utilService;

    public VendorService(MealRepository mealRepository, OrderRepository orderRepository,
                         UtilService utilService) {
        this.mealRepository = mealRepository;
        this.orderRepository = orderRepository;
        this.utilService = utilService;
    }

    public List<Map<String, Object>> listMeals() {
        List<Map<String, Object>> mealList = new ArrayList<>();
        List<Meal> meals = mealRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
        meals.forEach(meal -> mealList.add(utilService.generateMealMap(meal)));

        return mealList;
    }

    public Map<String, Object> addMeal(Meal meal) {
        String image = "/food/food_" + utilService.generateRandomNumber(1, 12) + ".jpg";
        meal.setImage(image);
        meal.setIsAvailable(meal.getIsAvailable() != null);

        mealRepository.save(meal);

        return utilService.generateMealMap(meal);
    }

    public Map<String, Object> getMeal(int id) {
        Optional<Meal> meal = mealRepository.findById(id);
        return meal.map(utilService::generateMealMap).orElse(null);
    }

    public Map<String, Object> updateMeal(Meal updatedMeal) {
        Optional<Meal> meal = mealRepository.findById(updatedMeal.getId());
        if (!meal.isPresent()) return null;

        meal.get().setName(updatedMeal.getName());
        meal.get().setImage(updatedMeal.getImage());
        meal.get().setDescription(updatedMeal.getDescription());
        meal.get().setIsAvailable(updatedMeal.getIsAvailable());
        mealRepository.save(meal.get());

        return utilService.generateMealMap(meal.get());
    }
}
