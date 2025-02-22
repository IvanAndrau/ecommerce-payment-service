package com.example.ecommerce_payment_service.Services;

import com.example.ecommerce_payment_service.Entities.Payment;
import com.example.ecommerce_payment_service.Entities.Refund;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IRefundService {

    // Start a refund process for a completed Payment
    //public Refund initiateRefund(Long paymentId);

    // method with optional parameter
    public Refund initiateRefund(Long paymentId, Double refundAmount);

    // Check the status of a Refund
    public Refund getRefundStatus(Long refundId);

    // Cancel a pending payment before it gets processed with status = CANCELLED
    public Payment cancelPayment(Long paymentId);

    // Retrieve a list of failed transactions for monitoring and alerts
    public List<Payment> getFailedPayments();
}
