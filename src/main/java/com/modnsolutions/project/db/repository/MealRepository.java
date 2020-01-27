package com.modnsolutions.project.db.repository;

import com.modnsolutions.project.db.entity.Meal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MealRepository extends JpaRepository<Meal, Integer> {
    Page<Meal> findAllByIsAvailableTrue(Pageable pageable);
    Optional<Meal> findByName(String name);
}
