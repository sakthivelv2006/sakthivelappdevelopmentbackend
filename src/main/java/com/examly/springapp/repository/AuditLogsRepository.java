package com.examly.springapp.repository;

import com.examly.springapp.model.Auditlogs;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AuditLogsRepository extends JpaRepository<Auditlogs, Long> {
List<Auditlogs> findByClaims_Id(Long claimId);
List<Auditlogs> findByUser_Id(Long userId);
}
