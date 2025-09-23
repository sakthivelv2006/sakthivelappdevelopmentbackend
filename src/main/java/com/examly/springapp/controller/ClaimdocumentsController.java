package com.examly.springapp.controller;

import com.examly.springapp.model.Claimdocuments;
import com.examly.springapp.model.Claims;
import com.examly.springapp.model.Customer;
import com.examly.springapp.repository.ClaimdocumentsRepository;
import com.examly.springapp.repository.ClaimsRepository;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ClaimdocumentsController {

@Autowired
private ClaimdocumentsRepository claimdocumentsRepository;

@Autowired
private ClaimsRepository claimsRepository;





@PostMapping("/api/claims/{claimId}/{customerId}/documents")
public ResponseEntity<?> uploadDocument(
@PathVariable Long claimId,
@PathVariable Long customerId,
@RequestBody @Valid claimdocumentdto dto) {
    try {
    Claims claim = claimsRepository.findById(claimId)
    .orElseThrow(() -> new RuntimeException("Claim not found"));

    if (!claim.getCustomer().getId().equals(customerId)) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
    .body("Claim does not belong to the specified customer");
    }

    boolean exists = claimdocumentsRepository.existsByClaimId_IdAndCustomer_Id(claimId, customerId);
    if (exists) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
    .body("Document for this claim and customer already exists");
    }

    Claimdocuments document = new Claimdocuments();
    document.setClaimId(claim);
    document.setCustomer(claim.getCustomer());
    document.setDocument_name(dto.getDocumentName());
    document.setFile_url(dto.getFileUrl());

    Claimdocuments savedDocument = claimdocumentsRepository.save(document);

    return ResponseEntity.status(HttpStatus.CREATED).body(savedDocument);

    } catch (Exception e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
    }
    }


    





@GetMapping("/api/claims/{claimId}/documents")
public List<Claimdocuments> getDocumentsByClaim(@PathVariable Long claimId) {
return claimdocumentsRepository.findByClaimId_Id(claimId);
}
@GetMapping("/api/claim-documents/{id}")
public ResponseEntity<Claimdocuments> getDocumentById(@PathVariable Long id) {
    
    return claimdocumentsRepository.findById(id)
    .map(ResponseEntity::ok)
    .orElse(ResponseEntity.notFound().build());
   

}
@DeleteMapping("/api/claim-documents/{id}")
public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
return claimdocumentsRepository.findById(id)
.map(existing->{
claimdocumentsRepository.delete(existing);
return ResponseEntity.noContent().<Void>build();
})
.orElse(ResponseEntity.notFound().build());
}
}