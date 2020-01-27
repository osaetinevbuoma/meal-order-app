package com.modnsolutions.project.db.repository;

import com.modnsolutions.project.db.entity.MealOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MealOrderRepository extends JpaRepository<MealOrder, Integer> {
    List<MealOrder> findAllByIsPlacedNowIsTrue();
}
