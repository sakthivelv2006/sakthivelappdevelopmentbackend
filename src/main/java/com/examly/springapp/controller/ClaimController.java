package com.examly.springapp.controller;

import com.examly.springapp.model.Claim;
import com.examly.springapp.service.ClaimService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ClaimController {

    @Autowired
    private ClaimService claimService;

    @PostMapping("/api/claims")
    public Claim submitClaim(@Valid @RequestBody Claim claim) {
        return claimService.submitClaim(claim);
    }

    @GetMapping("/api/claims")
    public List<Claim> getAllClaims() {
        return claimService.getAllClaims();
    }

    @GetMapping("/api/claims/{id}")
    public Claim getClaimById(@PathVariable Long id) {
        return claimService.getClaimById(id);
    }

    @GetMapping("/api/customers/{customerId}/claims")
    public List<Claim> getClaimsByCustomerId(@PathVariable Long customerId) {
        return claimService.getClaimsByCustomerId(customerId);
    }

    @PutMapping("/api/claims/{id}/status")
    public Claim updateClaimStatus(@PathVariable Long id, @RequestBody Map<String, String> statusMap) {
        return claimService.updateClaimStatus(id, statusMap.get("status"));
    }
}
