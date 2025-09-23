package com.examly.springapp.repository;

import com.examly.springapp.model.Claims;
import com.examly.springapp.model.Order;
import com.examly.springapp.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
Order findByOrderId(String orderId);
boolean existsByUserAndClaims(User user, Claims claims);

}
