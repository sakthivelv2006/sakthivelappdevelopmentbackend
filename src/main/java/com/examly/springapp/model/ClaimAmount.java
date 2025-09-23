package com.examly.springapp.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "claim_amounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaimAmount {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@ManyToOne
@JoinColumn(name = "user_id", nullable = false)
private User user;

@ManyToOne
@JoinColumn(name = "claims_id", nullable = false)
private Claims claim;

@Column(nullable = false, unique = true)
private String claimReferenceId;

@Column(nullable = false)
private Double claimAmount;


@Column(nullable = false)
private String bankAccountHolderName;

@Column(nullable = false)
private String bankAccountNumber;

@Column(nullable = false)
private String bankIfscCode;

private String status = "PENDING";

private LocalDateTime createdAt = LocalDateTime.now();
}
