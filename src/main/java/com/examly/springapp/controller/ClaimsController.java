package com.examly.springapp.controller;

import com.examly.springapp.enumclass.ClaimStatus;
import com.examly.springapp.model.Auditlogs;
import com.examly.springapp.model.Claim;
import com.examly.springapp.model.ClaimTypes;
import com.examly.springapp.model.Claims;
import com.examly.springapp.model.User;
import com.examly.springapp.repository.AuditLogsRepository;
import com.examly.springapp.repository.ClaimTypesRepository;
import com.examly.springapp.repository.ClaimsRepository;
import com.examly.springapp.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/claims-extended")
public class ClaimsController {

@Autowired
private ClaimsRepository claimsRepository;

@Autowired 
UserRepository userRepository;

@Autowired
ClaimTypesRepository claimTypesRepository;


@Autowired
AuditLogsRepository auditLogsRepository;

@PostMapping
public ResponseEntity<?> createClaim(@RequestBody ClaimDTO dto) {
    try {

boolean alreadyExists = claimsRepository.existsByCustomer_IdAndClaimType_ClaimtypeId(dto.getCustomerId(), dto.getClaimtypeId());
if (alreadyExists) {
return ResponseEntity.status(HttpStatus.CONFLICT)
.body("Error: User has already applied for this claim type");
}


    User user = userRepository.findById(dto.getCustomerId())
    .orElseThrow(() -> new RuntimeException("User not found"));

    ClaimTypes claimType = claimTypesRepository.findById(dto.getClaimtypeId())
    .orElseThrow(() -> new RuntimeException("ClaimType not found"));

    Claims claim = new Claims();
    claim.setCustomer(user);
    claim.setClaimType(claimType);
    claim.setClaimAmount(dto.getClaimAmount());
    claim.setIncidentDate(dto.getIncidentDate());
    claim.setDescription(dto.getDescription());

    ClaimStatus status;
    try {
    status = ClaimStatus.valueOf(dto.getStatus());
    } catch (Exception e) {
    status = ClaimStatus.IN_REVIEW;
    }
    claim.setStatus(status);

    claim.setSubmissionDate(dto.getSubmissionDate() != null ? dto.getSubmissionDate() : LocalDate.now());

    Claims savedClaim = claimsRepository.save(claim);

    Auditlogs auditlog = new Auditlogs();
    
        auditlog.setClaims(savedClaim);
    auditlog.setUser(user);
    auditlog.setAction("Claim created");
    auditlog.setTimestamp(LocalDateTime.now());
    auditLogsRepository.save(auditlog);

    return ResponseEntity.status(HttpStatus.CREATED).body(savedClaim);

    } catch (Exception e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
    }
    }





@GetMapping
public List<Claims> getAllClaims() {
return claimsRepository.findAll();
}

@GetMapping("/{id}")
public Claims getClaimById(@PathVariable Long id) {
return claimsRepository.findById(id)
.orElseThrow(() -> new RuntimeException("Claim not found with ID: " + id));
}

@PutMapping("/{id}/status")
public Claims updateClaimStatus(@PathVariable Long id, @RequestBody Map<String, String> statusMap) {
System.out.println("Update claim status called for id: " + id);
System.out.println("New status: " + statusMap.get("status"));

Claims claim = claimsRepository.findById(id)
.orElseThrow(() -> new RuntimeException("Claim not found with ID: " + id));

claim.setStatus(ClaimStatus.valueOf(statusMap.get("status")));

Claims updated = claimsRepository.save(claim);
System.out.println("Claim status updated to: " + updated.getStatus());
return updated;
}

@GetMapping("/user/{userId}")
public ResponseEntity<List<Claims>>getClaimsByUserId(@PathVariable Long userId) {
List<Claims>claims = claimsRepository.findByCustomerId(userId);
return ResponseEntity.ok(claims);
}


}
