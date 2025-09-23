package com.examly.springapp.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;


@ManyToOne
@JoinColumn(name = "users_id", nullable = false)
private User user;

@ManyToOne
@JoinColumn(name = "claims_id", nullable = false)
private Claims claims;

@Column(nullable = false, unique = true)
private String orderId;

@Column(nullable = false)
private Double amount;

@Column(nullable = false)
private String currency = "INR";

private String receipt;

private String status;

private LocalDateTime createdAt = LocalDateTime.now();
}
