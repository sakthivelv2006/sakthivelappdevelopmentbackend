package com.examly.springapp.repository;


import com.examly.springapp.model.Claims;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaimsRepository extends JpaRepository<Claims, Long> {
    Optional<Claims> findById(Long id);
    List<Claims> findByCustomerId(Long customer);
   boolean existsByIdAndCustomerId(Long id, Long customerId);
   boolean existsByCustomer_IdAndClaimType_ClaimtypeId(Long customerId, Long claimtypeId);
   
   
}
