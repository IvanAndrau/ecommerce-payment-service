package com.example.ecommerce_payment_service.Controllers;


import com.example.ecommerce_payment_service.Entities.Payment;
import com.example.ecommerce_payment_service.Entities.Refund;
import com.example.ecommerce_payment_service.Exceptions.InvalidRefundException;
import com.example.ecommerce_payment_service.Exceptions.RefundNotFoundException;
import com.example.ecommerce_payment_service.Services.IPaymentService;
import com.example.ecommerce_payment_service.Services.IRefundService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
@Slf4j
@Tag(name = "Refund Controller", description = "API for managing refunds")
@SecurityRequirement(name = "bearerAuth") // <==== All endpoints require JWT **except where overridden**
public class RefundController {
    private final IPaymentService paymentService;
    private final IRefundService refundService;

    public RefundController(@Qualifier("paymentServiceImpl")IPaymentService paymentService,
                             @Qualifier("refundServiceImpl")IRefundService refundService) {
        this.paymentService = paymentService;
        this.refundService = refundService;
    }

    @Operation(summary = "Initiate refund", description = "Initiates a refund for a given payment ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Refund initiated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid refund amount"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @PostMapping("/refund/{paymentId}")
    public ResponseEntity<Refund> initiateRefund(@PathVariable("paymentId") Long paymentId, Optional<Double> refundAmount) {
        if(refundAmount.get() < 0.0) {
            throw new InvalidRefundException("Refund amount cannot be negative");
        }
        Refund refund = refundService.initiateRefund(paymentId, refundAmount.orElse(null));

        return ResponseEntity.ok(refund);
    }

    @Operation(summary = "Get refund by ID", description = "Retrieves refund details for the given refund ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Refund details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Refund not found")
    })
    @Tag(name = "get", description = "get refund by refund id")
    @GetMapping("/refund/{refundId}")
    public ResponseEntity<Refund> getRefundById(@PathVariable("refundId") Long refundId) {
        if(refundService.getRefundStatus(refundId) == null) {
            throw new RefundNotFoundException("Refund  with id:" + refundId + "not found");
        }
        Refund refund = refundService.getRefundStatus(refundId);

        return ResponseEntity.ok(refund);
    }

    @Operation(summary = "Cancel payment", description = "Cancels a payment by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment canceled successfully"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @PostMapping("/cancel/{paymentId}")
    public ResponseEntity<Payment> cancelPayment(@PathVariable("paymentId") Long paymentId) {
        Payment payment = paymentService.getPaymentById(paymentId);

        return ResponseEntity.ok(payment);
    }

    @Operation(summary = "Get failed payments", description = "Retrieves a list of failed payments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of failed payments retrieved successfully")
    })
    @GetMapping("/failed")
    public List<Payment> getPaymentFailed() {
        List<Payment> payments = refundService.getFailedPayments();

        return payments;
    }
}
