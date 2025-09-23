package com.examly.springapp.repository;

import com.examly.springapp.model.ClaimAmount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClaimAmountRepository extends JpaRepository<ClaimAmount, Long> {
}
