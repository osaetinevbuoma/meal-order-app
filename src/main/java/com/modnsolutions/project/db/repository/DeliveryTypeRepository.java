package com.modnsolutions.project.db.repository;

import com.modnsolutions.project.db.entity.DeliveryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeliveryTypeRepository extends JpaRepository<DeliveryType, Integer> {
    Optional<DeliveryType> findByType(String type);
}
