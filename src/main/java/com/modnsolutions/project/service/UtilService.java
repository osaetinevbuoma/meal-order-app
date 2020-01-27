package com.modnsolutions.project.service;

import com.modnsolutions.project.db.entity.*;
import ng.com.byteworks.project.db.entity.*;
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

    public Map<String, Object> generateMealOrderMap(MealOrder mealOrder) {
        Map<String, Object> mealOrderMap = new HashMap<>();
        mealOrderMap.put("id", mealOrder.getId());
        mealOrderMap.put("isPlacedNow", mealOrder.getIsPlacedNow());
        mealOrderMap.put("isPaid", mealOrder.getIsPaid());
        mealOrderMap.put("isDispatched", mealOrder.getIsDispatched());
        mealOrderMap.put("reference", mealOrder.getReference());
        mealOrderMap.put("user", generateUserMap(mealOrder.getUser()));
        mealOrderMap.put("paymentOption", generatePaymentOptionMap(mealOrder.getPaymentOption()));
        mealOrderMap.put("deliveryType", generateDeliveryTypeMap(mealOrder.getDeliveryType()));

        return mealOrderMap;
    }

    public Map<String, Object> generateOrderedMealMap(OrderedMeal orderedMeal) {
        Map<String, Object> orderedMealMap = new HashMap<>();
        orderedMealMap.put("id", orderedMeal.getId());
        orderedMealMap.put("name", orderedMeal.getName());
        orderedMealMap.put("price", orderedMeal.getPrice());
        orderedMealMap.put("quantity", orderedMeal.getQuantity());
        orderedMealMap.put("meal", generateMealMap(orderedMeal.getMeal()));

        return orderedMealMap;
    }

    public Map<String, Object> generateUserMap(User user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", user.getId());
        userMap.put("firstName", user.getFirstName());
        userMap.put("lastName", user.getLastName());
        userMap.put("email", user.getEmail());

        return userMap;
    }

    public Map<String, Object> generatePaymentOptionMap(PaymentOption paymentOption) {
        Map<String, Object> optionMap = new HashMap<>();
        optionMap.put("id", paymentOption.getId());
        optionMap.put("discount", paymentOption.getDiscount());
        optionMap.put("option", paymentOption.getOption());

        return optionMap;
    }

    public Map<String, Object> generateDeliveryTypeMap(DeliveryType deliveryType) {
        Map<String, Object> deliveryTypeMap = new HashMap<>();
        deliveryTypeMap.put("id", deliveryType.getId());
        deliveryTypeMap.put("type", deliveryType.getType());
        deliveryTypeMap.put("amount", deliveryType.getAmount());

        return deliveryTypeMap;
    }
}
