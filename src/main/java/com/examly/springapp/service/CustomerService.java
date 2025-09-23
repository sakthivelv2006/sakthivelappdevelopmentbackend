package com.examly.springapp.service;

import com.examly.springapp.exception.ResourceNotFoundException;
import com.examly.springapp.exception.ValidationException;
import com.examly.springapp.model.Customer;
import com.examly.springapp.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Customer createCustomer(Customer customer) {
        if (customer.getName() == null || customer.getName().isBlank()
                || customer.getEmail() == null || !customer.getEmail().contains("@")
                || customer.getPhoneNumber() == null || customer.getPhoneNumber().length() < 5
                || customerRepository.existsByEmailAndPolicyNumber(customer.getEmail(), customer.getPolicyNumber())) {

            throw new ValidationException("Invalid or duplicate customer data");
        }
        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers() {

        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("not found"));
    }

    public List<Customer> getCustomersByEmail(String email) {
        List<Customer> customers = customerRepository.findAllByEmail(email);
        if (customers.isEmpty()) {
            throw new ResourceNotFoundException("No customers found with email: " + email);
        }
        return customers;
    }

    public boolean existsByEmail(String email) {
        return customerRepository.existsByEmail(email);
    }

}
