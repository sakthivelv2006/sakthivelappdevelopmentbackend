package com.examly.springapp.controller;

import com.examly.springapp.model.Customer;
import com.examly.springapp.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CustomerService customerService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateCustomer_Success() throws Exception {
        Customer input = new Customer(null, "John Doe", "john.doe@example.com", "1234567890", "POL-12345");
        Customer output = new Customer(1L, "John Doe", "john.doe@example.com", "1234567890", "POL-12345");
        Mockito.when(customerService.createCustomer(Mockito.any())).thenReturn(output);

        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerId", is(1)))
                .andExpect(jsonPath("$.name", is("John Doe")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")));
    }

    @Test
    public void testCreateCustomer_ValidationError() throws Exception {
        Customer input = new Customer(null, "", "invalid", "", "");
        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", not(emptyOrNullString())));
    }

    @Test
    public void testGetAllCustomers() throws Exception {
        Mockito.when(customerService.getAllCustomers()).thenReturn(Arrays.asList(
                new Customer(1L, "John Doe", "john.doe@example.com", "1234567890", "POL-12345"),
                new Customer(2L, "Jane Smith", "jane.smith@example.com", "0987654321", "POL-56789")
        ));
        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("John Doe")));
    }

    @Test
    public void testGetCustomerById_Success() throws Exception {
        Customer customer = new Customer(1L, "John Doe", "john.doe@example.com", "1234567890", "POL-12345");
        Mockito.when(customerService.getCustomerById(1L)).thenReturn(customer);
        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId", is(1)))
                .andExpect(jsonPath("$.name", is("John Doe")));
    }

    @Test
    public void testGetCustomerById_NotFound() throws Exception {
        Mockito.when(customerService.getCustomerById(99L)).thenThrow(new com.examly.springapp.exception.ResourceNotFoundException("Customer not found"));
        mockMvc.perform(get("/api/customers/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("not found")));
    }
}
