package com.example.ecommerce_payment_service.Services;

import com.example.ecommerce_payment_service.Entities.Payment;
import com.example.ecommerce_payment_service.Entities.PaymentStatus;
import com.example.ecommerce_payment_service.Repositories.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.util.UUID.randomUUID;

@Service
@Slf4j
public class PaymentServiceImpl implements IPaymentService{

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    // Creates a new Payment object with provided properties
    @Override
    public Payment createPayment(Long orderId, BigDecimal amount, String paymentMethod) {

        Payment payment = new Payment();

        payment.setOrderId(orderId);
        payment.setAmount(amount);
        payment.setStatus(PaymentStatus.INITIATED);
        payment.setTransactionId(paymentMethod + "_" + randomUUID()); //Generate unique identifier using paymentMethod and unique UUID

        return paymentRepository.save(payment);
    }

    // Process the payment and interact with third-parties gateways
    @Override
    public Payment processPayment(Payment payment)
    {
        // imitation of status after interacting with third-party gateway
        boolean thirdPartyServiceAcceptedPayment;
        Payment ongoingPayment = new Payment();

        ongoingPayment = paymentRepository.getReferenceById(payment.getId());
        // Place to insert third-party gateway to handle payment
        thirdPartyServiceAcceptedPayment = true;

        if(thirdPartyServiceAcceptedPayment) {
            ongoingPayment.setStatus(PaymentStatus.COMPLETED);
        }
        else {  // if third-party gateway rejected the payment
            ongoingPayment.setStatus(PaymentStatus.CANCELLED);
        }

        return ongoingPayment;
    }

    // Retrieves details of a specific payment
    @Override
    public Payment getPaymentById(Long paymentId)
    {
        Payment payment = new Payment();
        payment = paymentRepository.getReferenceById(paymentId);

        return payment;
    }

    // Retrieves a list of payments for a specific user
    @Override
    public List<Payment> getPaymentsByUserId(Long userId)
    {
        List<Payment> payments = new ArrayList<>();
        payments = paymentRepository.findByUserId(userId);

        return payments;
    }

    //Fetch payment details related to a specific order
    @Override
    public List<Payment> getPaymentsByOrderId(Long orderId)
    {
        List<Payment> payments = new ArrayList<>();
        payments = paymentRepository.findByOrderId(orderId);

        return payments;
    }
}
