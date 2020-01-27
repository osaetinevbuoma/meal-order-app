package ng.com.byteworks.project.db.repository;

import ng.com.byteworks.project.db.entity.MealOrder;
import ng.com.byteworks.project.db.entity.OrderedMeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderedMealRepository extends JpaRepository<OrderedMeal, Integer> {
    List<OrderedMeal> findAllByMealOrder(MealOrder mealOrder);
}
