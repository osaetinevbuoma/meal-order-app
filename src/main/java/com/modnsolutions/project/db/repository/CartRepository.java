package com.modnsolutions.project.db.repository;

import com.modnsolutions.project.db.entity.Cart;
import com.modnsolutions.project.db.entity.Meal;
import com.modnsolutions.project.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    List<Cart> findAllByUser(User user);
    Optional<Cart> findByMeal(Meal meal);
    Optional<Cart> findByIdAndUser(Integer id, User user);
}
