package com.modnsolutions.project.service;

import com.modnsolutions.project.db.entity.Cart;
import com.modnsolutions.project.db.entity.MealOrder;
import com.modnsolutions.project.db.entity.User;
import com.modnsolutions.project.db.repository.CartRepository;
import com.modnsolutions.project.db.repository.MealOrderRepository;
import com.modnsolutions.project.db.repository.OrderedMealRepository;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Secured("ROLE_DEVELOPER")
public class MealOrderService {
    private final AuthenticationService authenticationService;
    private final CartRepository cartRepository;
    private final MealOrderRepository mealOrderRepository;
    private final OrderedMealRepository orderedMealRepository;

    public MealOrderService(AuthenticationService authenticationService, CartRepository cartRepository,
                            MealOrderRepository mealOrderRepository,
                            OrderedMealRepository orderedMealRepository) {
        this.authenticationService = authenticationService;
        this.cartRepository = cartRepository;
        this.mealOrderRepository = mealOrderRepository;
        this.orderedMealRepository = orderedMealRepository;
    }

    public void saveOrder(MealOrder order) {
        User user = authenticationService.getAuthenticatedUser().getUser();
        order.setUser(user);
        order.getOrderedMeals().forEach(orderedMeal -> orderedMeal.setMealOrder(order));

        List<Cart> carts = cartRepository.findAllByUser(user);
        cartRepository.deleteAll(carts);
        mealOrderRepository.save(order);
        orderedMealRepository.saveAll(order.getOrderedMeals());
    }
}
