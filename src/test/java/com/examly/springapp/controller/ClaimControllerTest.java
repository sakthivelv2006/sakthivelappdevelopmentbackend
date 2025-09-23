package com.examly.springapp.controller;

import com.examly.springapp.model.Claim;
import com.examly.springapp.model.Customer;
import com.examly.springapp.service.ClaimService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClaimController.class)
public class ClaimControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ClaimService claimService;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void testSubmitClaim_ValidationError() throws Exception {
        Map<String, Object> claimRequest = new HashMap<>();
        claimRequest.put("customerId", 1L); // missing other fields
        mockMvc.perform(post("/api/claims")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(claimRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", not(emptyOrNullString())));
    }

    @Test
    public void testGetAllClaims() throws Exception {
        Claim c1 = new Claim(1L, null, "Auto", 1200.00, LocalDate.of(2023,1,20), "Accident", "APPROVED", LocalDate.of(2023,1,21));
        Claim c2 = new Claim(2L, null, "Property", 3500.00, LocalDate.of(2023,2,11), "Storm damage", "REJECTED", LocalDate.of(2023,2,12));
        Mockito.when(claimService.getAllClaims()).thenReturn(Arrays.asList(c1, c2));
        mockMvc.perform(get("/api/claims"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].claimType", is("Auto")));
    }

    @Test
    public void testGetClaimById_Success() throws Exception {
        Claim claim = new Claim(1L, null, "Auto", 1200.00, LocalDate.of(2023,1,20), "Accident", "APPROVED", LocalDate.of(2023,1,21));
        Mockito.when(claimService.getClaimById(1L)).thenReturn(claim);
        mockMvc.perform(get("/api/claims/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.claimType", is("Auto")));
    }

    @Test
    public void testGetClaimsByCustomerId_Success() throws Exception {
        Claim c1 = new Claim(1L, null, "Auto", 1200.00, LocalDate.of(2023,1,20), "Accident", "APPROVED", LocalDate.of(2023,1,21));
        Claim c2 = new Claim(2L, null, "Property", 3500.00, LocalDate.of(2023,2,11), "Storm damage", "REJECTED", LocalDate.of(2023,2,12));
        Mockito.when(claimService.getClaimsByCustomerId(1L)).thenReturn(Arrays.asList(c1, c2));
        mockMvc.perform(get("/api/customers/1/claims"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].claimType", is("Auto")));
    }

    @Test
    public void testUpdateClaimStatus_Success() throws Exception {
        Claim originalClaim = new Claim(1L, null, "Auto", 1200.00, LocalDate.of(2023,1,20), "Accident", "SUBMITTED", LocalDate.of(2023,1,21));
        Claim updatedClaim = new Claim(1L, null, "Auto", 1200.00, LocalDate.of(2023,1,20), "Accident", "APPROVED", LocalDate.of(2023,1,21));
        Map<String, String> statusReq = new HashMap<>();
        statusReq.put("status", "APPROVED");
        Mockito.when(claimService.updateClaimStatus(1L, "APPROVED")).thenReturn(updatedClaim);

        mockMvc.perform(put("/api/claims/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(statusReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("APPROVED")));
    }

    @Test
    public void testUpdateClaimStatus_InvalidStatus() throws Exception {
        Map<String, String> statusReq = new HashMap<>();
        statusReq.put("status", "INVALID");
        Mockito.when(claimService.updateClaimStatus(1L, "INVALID")).thenThrow(new com.examly.springapp.exception.ValidationException("Invalid claim status"));
        mockMvc.perform(put("/api/claims/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(statusReq)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Invalid claim status")));
    }
}
