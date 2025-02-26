package com.example.ecommerce_payment_service.Controllers;


import com.example.ecommerce_payment_service.Entities.Payment;
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

    public PaymentController(@Qualifier("paymentServiceImpl")IPaymentService paymentService) {
        this.paymentService = paymentService;
    }


    // Integrate custom exceptions like
    // PaymentNotFoundException, InvalidRefundException, and OrderNotFoundException
    @PostMapping
    public ResponseEntity<Payment> createPayment(@Valid @RequestBody Long orderId,
                                                 BigDecimal amount, String paymentMethod) {
        Payment payment = paymentService.createPayment(orderId, amount, paymentMethod);
        log.info("Payment created: {}", payment);
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
}
