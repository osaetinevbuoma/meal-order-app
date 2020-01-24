package ng.com.byteworks.project.service;

import ng.com.byteworks.project.db.entity.Cart;
import ng.com.byteworks.project.db.entity.Meal;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UtilService {
    public int generateRandomNumber(int min, int max) {
        return (int) (Math.random() * ((max - min) + 1)) + min;
    }

    public Map<String, Object> generateMealMap(Meal meal) {
        Map<String, Object> mealMap = new HashMap<>();
        mealMap.put("id", meal.getId());
        mealMap.put("name", meal.getName());
        mealMap.put("image", meal.getImage());
        mealMap.put("price", meal.getPrice());
        mealMap.put("description", meal.getDescription());
        mealMap.put("isAvailable", meal.getIsAvailable());

        return mealMap;
    }

    public Map<String, Object> generateCartMap(Cart cart) {
        Map<String, Object> cartMap = new HashMap<>();
        cartMap.put("id", cart.getId());
        cartMap.put("quantity", cart.getQuantity());
        cartMap.put("meal", generateMealMap(cart.getMeal()));

        return cartMap;
    }
}
