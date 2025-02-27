package com.example.ecommerce_payment_service.Controllers;


import com.example.ecommerce_payment_service.Entities.Payment;
import com.example.ecommerce_payment_service.Entities.Refund;
import com.example.ecommerce_payment_service.Exceptions.InvalidRefundException;
import com.example.ecommerce_payment_service.Exceptions.RefundNotFoundException;
import com.example.ecommerce_payment_service.Services.IPaymentService;
import com.example.ecommerce_payment_service.Services.IRefundService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
@Slf4j
public class RefundController {
    private final IPaymentService paymentService;
    private final IRefundService refundService;

    public RefundController(@Qualifier("paymentServiceImpl")IPaymentService paymentService,
                             @Qualifier("refundServiceImpl")IRefundService refundService) {
        this.paymentService = paymentService;
        this.refundService = refundService;
    }


    @PostMapping("/refund/{paymentId}")
    public ResponseEntity<Refund> initiateRefund(@PathVariable("paymentId") Long paymentId, Optional<Double> refundAmount) {
        if(refundAmount.get() < 0.0) {
            throw new InvalidRefundException("Refund amount cannot be negative");
        }
        Refund refund = refundService.initiateRefund(paymentId, refundAmount.orElse(null));

        return ResponseEntity.ok(refund);
    }

    @GetMapping("/refund/{refundId}")
    public ResponseEntity<Refund> getRefundById(@PathVariable("refundId") Long refundId) {
        if(refundService.getRefundStatus(refundId) == null) {
            throw new RefundNotFoundException("Refund  with id:" + refundId + "not found");
        }
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
