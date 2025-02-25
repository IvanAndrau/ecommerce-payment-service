package com.example.ecommerce_payment_service.Controllers;


import com.example.ecommerce_payment_service.Entities.Payment;
import com.example.ecommerce_payment_service.Entities.PaymentStatus;
import com.example.ecommerce_payment_service.Entities.Refund;
import com.example.ecommerce_payment_service.Services.IPaymentService;
import com.example.ecommerce_payment_service.Services.IRefundService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
@Slf4j
public class PaymentController {
    private final IPaymentService paymentService;
    private final IRefundService refundService;

    public PaymentController(@Qualifier("paymentServiceImpl")IPaymentService paymentService,
                              @Qualifier("refundServiceImpl")IRefundService refundService) {
        this.paymentService = paymentService;
        this.refundService = refundService;
    }


    // Initiate a new payment for an order
    @PostMapping
    public ResponseEntity<Payment> createPayment(@Valid @RequestBody Long orderId,
                                                 BigDecimal amount, String paymentMethod) {
        Payment payment = paymentService.createPayment(orderId, amount, paymentMethod);
        log.info("Payment created: " + payment);
        return ResponseEntity.ok(payment);
    }


    @PostMapping("/process/{paymentId}")
    public ResponseEntity<Payment> processPayment(@PathVariable("paymentId") Long paymentId) {
        Payment payment = paymentService.getPaymentById(paymentId);
        payment = paymentService.processPayment(payment);

        return ResponseEntity.ok(payment);
    }


    @GetMapping("/{paymentId}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable("paymentId") Long paymentId) {
        Payment payment = paymentService.getPaymentById(paymentId);

        return ResponseEntity.ok(payment);
    }

    @GetMapping("/user/{userId}")
    public List<Payment> getPaymentByUserId(@PathVariable("userId") Long userId) {
        List<Payment> payments = paymentService.getPaymentsByUserId(userId);

        return payments;
    }


    @GetMapping("/order/{orderId}")
    public List<Payment> getPaymentByOrderId(@PathVariable("orderId") Long orderId) {
        List<Payment> payments = paymentService.getPaymentsByOrderId(orderId);

        return payments;
    }

    // Refund methods

    @PostMapping("/refund/{paymentId}")
    public ResponseEntity<Refund> initiateRefund(@PathVariable("paymentId") Long paymentId, Optional<Double> refundAmount) {
        Refund refund = refundService.initiateRefund(paymentId, refundAmount);

        return ResponseEntity.ok(refund);
    }

    @GetMapping("/refund/{refundId}")
    public ResponseEntity<Refund> getRefundById(@PathVariable("refundId") Long refundId) {
        Refund refund = refundService.getRefundStatus(refundId);

        return ResponseEntity.ok(refund);
    }

    @PostMapping("/cancel/{paymentId}")
    public ResponseEntity<Payment> cancelPayment(@PathVariable("paymentId") Long paymentId) {
        Payment payment = paymentService.getPaymentById(paymentId);

        return ResponseEntity.ok(payment);
    }

    @GetMapping("/failed")
    public List<Payment> getPaymentFailed() {
        List<Payment> payments = refundService.getFailedPayments();

        return payments;
    }
}
