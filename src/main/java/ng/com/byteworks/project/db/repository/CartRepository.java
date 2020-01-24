package ng.com.byteworks.project.db.repository;

import ng.com.byteworks.project.db.entity.Cart;
import ng.com.byteworks.project.db.entity.Meal;
import ng.com.byteworks.project.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    List<Cart> findAllByUser(User user);
    Optional<Cart> findByMeal(Meal meal);
    Optional<Cart> findByIdAndUser(Integer id, User user);
}
