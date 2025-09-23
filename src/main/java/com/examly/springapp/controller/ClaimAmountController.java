package com.examly.springapp.controller;

import com.examly.springapp.model.ClaimAmount;
import com.examly.springapp.repository.ClaimAmountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/amounts/claims")
public class ClaimAmountController {

@Autowired
private ClaimAmountRepository claimAmountRepository;

@PostMapping("/submit")
public ClaimAmount submitClaim(@RequestBody ClaimAmount claimAmount) {
return claimAmountRepository.save(claimAmount);
}

@GetMapping
public List<ClaimAmount> getAllClaims() {
return claimAmountRepository.findAll();
}

@GetMapping("/{id}")
public ClaimAmount getClaimById(@PathVariable Long id) {
return claimAmountRepository.findById(id).orElse(null);
}
}
