package com.example.ecommerce_payment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentConfirmedEvent {
    private Long orderId;
    private String status; // Should match the enum in the order service
}
