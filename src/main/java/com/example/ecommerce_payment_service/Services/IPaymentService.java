package com.example.ecommerce_payment_service.Services;

import com.example.ecommerce_payment_service.Entities.Payment;
import com.example.ecommerce_payment_service.Repositories.PaymentRepository;

public interface IPaymentService {
    Payment sendPayment(Payment payment);
}
