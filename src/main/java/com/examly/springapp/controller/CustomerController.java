package com.examly.springapp.controller;

import com.examly.springapp.exception.ResourceNotFoundException;
import com.examly.springapp.model.Customer;
import com.examly.springapp.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer) {
        Customer saved = customerService.createCustomer(customer);
        return ResponseEntity.status(201).body(saved);
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        System.out.println("eorrr");
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    @PostMapping("/login")
    public ResponseEntity<Boolean> loginCustomer(@RequestBody Customer customer) {
        boolean exists = customerService.existsByEmail(customer.getEmail());
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<List<Customer>> getCustomerByEmail(@PathVariable String email) {
        List<Customer> customers = customerService.getCustomersByEmail(email);
        return ResponseEntity.ok(customers);
    }

}
