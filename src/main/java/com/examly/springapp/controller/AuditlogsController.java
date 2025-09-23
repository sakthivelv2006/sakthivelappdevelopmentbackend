package com.examly.springapp.controller;

import com.examly.springapp.model.Auditlogs;
import com.examly.springapp.repository.AuditLogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class AuditlogsController {

@Autowired
private AuditLogsRepository auditLogsRepository;

@GetMapping("/api/audit-logs")
public List<Auditlogs> getAllAuditLogs() {
return auditLogsRepository.findAll();
}

@GetMapping("/api/audit-logs/{id}")
public Auditlogs getAuditLogById(@PathVariable Long id) {
return auditLogsRepository.findById(id).orElse(null);
}

@GetMapping("/api/claims/{claimId}/audit-logs")
public List<Auditlogs> getAuditLogsByClaimId(@PathVariable Long claimId) {
return auditLogsRepository.findByClaims_Id(claimId);
}

@GetMapping("/api/users/{userId}/audit-logs")
public List<Auditlogs> getAuditLogsByUserId(@PathVariable Long userId) {
return auditLogsRepository.findByUser_Id(userId);
}
}
