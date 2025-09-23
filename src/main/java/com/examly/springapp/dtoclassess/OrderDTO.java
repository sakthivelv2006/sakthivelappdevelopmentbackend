package com.examly.springapp.dtoclassess;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long userId;
    private Long claimsId;
private String orderId;
private Double amount;
private String currency;
private String receipt;
private String status;
}
