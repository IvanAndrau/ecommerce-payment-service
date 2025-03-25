package com.example.ecommerce_payment_service.Controllers;


import com.example.ecommerce_payment_service.Entities.Payment;
import com.example.ecommerce_payment_service.Exceptions.OrderNotFoundException;
import com.example.ecommerce_payment_service.Exceptions.PaymentNotFoundException;
import com.example.ecommerce_payment_service.Services.IPaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@Slf4j
@Tag(name = "Payment Controller", description = "API for managing payments")
@SecurityRequirement(name = "bearerAuth") // <==== All endpoints require JWT **except where overridden**
public class PaymentController {
    private final IPaymentService paymentService;

    public PaymentController(@Qualifier("paymentServiceImpl")IPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Operation(summary = "Create a new payment", description = "Creates a new payment for the given order ID and amount")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid payment details"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PostMapping
    public ResponseEntity<Payment> createPayment(@Valid @RequestBody Long orderId,
                                                 BigDecimal amount, String paymentMethod) {
        try {
            Payment payment = paymentService.createPayment(orderId, amount, paymentMethod);
            log.info("Payment created: {}", payment);
            return ResponseEntity.ok(payment);
        } catch (OrderNotFoundException ex) {
            throw new OrderNotFoundException("Order ID " + orderId + " not found.");
        }
    }

    @Operation(summary = "Process a payment", description = "Processes a payment for the given payment ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment processed successfully"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @PostMapping("/process/{paymentId}")
    public ResponseEntity<Payment> processPayment(@PathVariable("paymentId") Long paymentId) {
        Payment payment = paymentService.getPaymentById(paymentId);
        if (payment == null) {
            log.warn("Payment with id:{} cannot be found", paymentId);
            throw new PaymentNotFoundException("Payment not found with ID: " + paymentId);
        }
        payment = paymentService.processPayment(payment);
        return ResponseEntity.ok(payment);
    }

    @Operation(summary = "Get payment by ID", description = "Retrieves payment details for the given payment ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @GetMapping("/{paymentId}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable("paymentId") Long paymentId) {
        Payment payment = paymentService.getPaymentById(paymentId);
        if (payment == null) {
            throw new PaymentNotFoundException("Payment not found with ID: " + paymentId);
        }
        return ResponseEntity.ok(payment);
    }

    @Operation(summary = "Get payments by user ID", description = "Retrieves all payments associated with a given user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of payments retrieved successfully")
    })
    @GetMapping("/user/{userId}")
    public List<Payment> getPaymentByUserId(@PathVariable("userId") Long userId) {
        List<Payment> payments = paymentService.getPaymentsByUserId(userId);
        if(payments == null || payments.isEmpty()) {
            throw new PaymentNotFoundException("Payment not found with ID: " + userId);
        }
        return payments;
    }

    @Operation(summary = "Get payments by order ID", description = "Retrieves all payments associated with a given order ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of payments retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/order/{orderId}")
    public List<Payment> getPaymentByOrderId(@PathVariable("orderId") Long orderId) {
        List<Payment> payments = paymentService.getPaymentsByOrderId(orderId);
        if (payments == null || payments.isEmpty()) {
            throw new OrderNotFoundException("No payments found for Order ID: " + orderId);
        }
        return payments;
    }
}
