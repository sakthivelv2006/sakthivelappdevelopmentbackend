package com.examly.springapp.controller;

import com.examly.springapp.model.ClaimTypes;
import com.examly.springapp.repository.ClaimTypesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/claim-types")
public class ClaimTypesController {

    @Autowired
    private ClaimTypesRepository claimTypesRepository;

    @PostMapping
    public ResponseEntity<ClaimTypes> createClaimType(@RequestBody ClaimTypes claimType) {
    ClaimTypes saved = claimTypesRepository.save(claimType);
    return ResponseEntity.status(201).body(saved);
    }
    @GetMapping
    public List<ClaimTypes> getAllClaimTypes() {
    return claimTypesRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClaimTypes> getClaimTypeById(@PathVariable Long id) {
        return claimTypesRepository.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
        }
    

    @PutMapping("/{id}")
    public ResponseEntity<ClaimTypes> updateClaimType(@PathVariable Long id, @RequestBody ClaimTypes claimType) {
        return claimTypesRepository.findById(id)
        .map(existing -> {
        existing.setName(claimType.getName());
        return ResponseEntity.ok(claimTypesRepository.save(existing));
        })
        .orElse(ResponseEntity.notFound().build());
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteClaimType(@PathVariable Long id) {
        return claimTypesRepository.findById(id)
        .map(existing -> {
        claimTypesRepository.delete(existing);
        return ResponseEntity.noContent().<Void>build();
        })
        .orElse(ResponseEntity.notFound().build());
        }
    }

