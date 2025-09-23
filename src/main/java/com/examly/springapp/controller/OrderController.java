package com.examly.springapp.controller;

import com.examly.springapp.model.Claims;
import com.examly.springapp.model.Order;
import com.examly.springapp.model.User;
import com.examly.springapp.repository.ClaimsRepository;
import com.examly.springapp.repository.OrderRepository;
import com.examly.springapp.repository.UserRepository;
import com.examly.springapp.dtoclassess.OrderDTO;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

@Autowired
private OrderRepository orderRepository;


@Autowired
private UserRepository userRepository;

@Autowired
private ClaimsRepository claimsRepository;

private RazorpayClient razorpayClient;

public OrderController() throws RazorpayException {
this.razorpayClient = new RazorpayClient(
"rzp_test_4LPGV7rP4wBreT",
"14VPwmYvO6LmoLAyqRKYgE3G"
);
}

@PostMapping("/create")
public OrderDTO createOrder(
@RequestParam Long userId,
@RequestParam Long claimsId,
@RequestParam Double amount) throws RazorpayException {

    User user = userRepository.findById(userId)
    .orElseThrow(() -> new RuntimeException("User not found"));

    Claims claims = claimsRepository.findById(claimsId)
    .orElseThrow(() -> new RuntimeException("Claims not found"));

    //boolean alreadyExists = orderRepository.existsByUserAndClaims(user, claims);
    //if (alreadyExists) {
    //throw new RuntimeException("This user already claimed this claim.");
    //}

    JSONObject options = new JSONObject();
    options.put("amount", amount * 100); // Razorpay expects paise
    options.put("currency", "INR");
    options.put("receipt", "receipt_" + System.currentTimeMillis());

    com.razorpay.Order razorpayOrder = razorpayClient.orders.create(options);

    Order order = new Order();
    order.setUser(user);
    order.setClaims(claims);
    order.setOrderId(razorpayOrder.get("id"));
    order.setAmount(amount);
    order.setCurrency("INR");
    order.setReceipt(razorpayOrder.get("receipt"));
    order.setStatus(razorpayOrder.get("status"));
    order.setCreatedAt(LocalDateTime.now());

    orderRepository.save(order);

    return new OrderDTO(
    user.getId(),
    claims.getId(),
    order.getOrderId(),
    order.getAmount(),
    order.getCurrency(),
    order.getReceipt(),
    order.getStatus()
    );
    }
}