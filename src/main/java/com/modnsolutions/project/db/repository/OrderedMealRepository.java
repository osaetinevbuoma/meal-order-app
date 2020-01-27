package com.modnsolutions.project.db.repository;

import com.modnsolutions.project.db.entity.MealOrder;
import com.modnsolutions.project.db.entity.OrderedMeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderedMealRepository extends JpaRepository<OrderedMeal, Integer> {
    List<OrderedMeal> findAllByMealOrder(MealOrder mealOrder);
}
