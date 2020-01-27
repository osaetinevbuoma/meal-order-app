package ng.com.byteworks.project.db.repository;

import ng.com.byteworks.project.db.entity.DeliveryType;
import ng.com.byteworks.project.db.entity.MealOrder;
import ng.com.byteworks.project.db.entity.PaymentOption;
import ng.com.byteworks.project.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MealOrderRepository extends JpaRepository<MealOrder, Integer> {
    List<MealOrder> findAllByIsPlacedNowIsTrue();
}
