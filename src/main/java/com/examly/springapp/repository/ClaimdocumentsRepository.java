package com.examly.springapp.repository;

import com.examly.springapp.model.Claimdocuments;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClaimdocumentsRepository extends JpaRepository<Claimdocuments, Long> {
List<Claimdocuments> findByClaimId_Id(Long claimId);
boolean existsByIdAndCustomerId(Long id, Long customerId);
boolean existsByClaimId_IdAndCustomer_Id(Long claimId, Long customerId);

}
