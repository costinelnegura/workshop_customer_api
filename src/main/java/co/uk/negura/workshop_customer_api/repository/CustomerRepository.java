package co.uk.negura.workshop_customer_api.repository;

import co.uk.negura.workshop_customer_api.model.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long>{
    Optional<CustomerEntity> findById(Long id);
    Optional<CustomerEntity> findByEmail(String email);
}
