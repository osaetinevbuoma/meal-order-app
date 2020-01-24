package ng.com.byteworks.project.db.repository;

import ng.com.byteworks.project.db.entity.PaymentOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentOptionRepository extends JpaRepository<PaymentOption, Integer> {
    Optional<PaymentOption> findByOption(String option);
}
