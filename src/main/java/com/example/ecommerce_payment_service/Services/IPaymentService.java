package com.example.ecommerce_payment_service.Services;

import com.example.ecommerce_payment_service.Entities.Payment;
import com.example.ecommerce_payment_service.Entities.PaymentStatus;
import com.example.ecommerce_payment_service.Repositories.PaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface IPaymentService {

    // Saves a newly created Payment
    Payment createPayment(Long orderId, BigDecimal amount, String paymentMethod);

    // Process the payment and interact with third-parties gateways
    Payment processPayment(Payment payment);

    // Retrieves details of a specific payment
    Payment getPaymentById(Long id);

    // Retrieves a list of payments for a specific user
    List<Payment> getPaymentsByUserId(Long id);

    //Fetch payment details related to a specific order
    List<Payment> getPaymentsByOrderId(Long id);
}
