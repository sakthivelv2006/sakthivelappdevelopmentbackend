package com.examly.springapp.repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.examly.springapp.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByEmail(String email);
    boolean existsByEmailAndPolicyNumber(String email, String policyNumber);
    Optional<Customer> findByEmail(String email);
    List<Customer> findAllByEmail(String email);
}
