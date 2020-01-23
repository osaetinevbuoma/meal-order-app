package ng.com.byteworks.project.db.repository;

import ng.com.byteworks.project.db.entity.DeliveryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryTypeRepository extends JpaRepository<DeliveryType, Integer> {
}
