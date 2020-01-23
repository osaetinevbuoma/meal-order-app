package ng.com.byteworks.project.db.repository;

import ng.com.byteworks.project.db.entity.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MealRepository extends JpaRepository<Meal, Integer> {
}
