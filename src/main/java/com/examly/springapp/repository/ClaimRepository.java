package com.examly.springapp.repository;

import com.examly.springapp.model.Claim;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClaimRepository extends JpaRepository<Claim, Long> {
    List<Claim> findByCustomerId(Long customerId);
}
