package com.example.ecommerce_payment_service.Services;

import com.example.ecommerce_payment_service.Entities.Payment;
import com.example.ecommerce_payment_service.Entities.PaymentStatus;
import com.example.ecommerce_payment_service.Repositories.PaymentRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
        payment.setPaymentMethod(paymentMethod);

        // Generate unique transaction ID
        String transactionId = paymentMethod + "_" + randomUUID();
        payment.setTransactionId(transactionId);

        return paymentRepository.save(payment);
    }

    // Process the payment and interact with third-parties gateways
    @Override
    public Payment processPayment(Payment payment)
    {
        Payment ongoingPayment = paymentRepository.getReferenceById(payment.getId());

        try {
            // Convert to smallest currency unit (Stripe uses cents)
            long stripeAmount = ongoingPayment.getAmount().multiply(BigDecimal.valueOf(100)).longValue();

            // Create Stripe PaymentIntent
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(stripeAmount)
                    .setCurrency("usd")
                    .setDescription("Payment for Order #" + ongoingPayment.getOrderId())
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            // Update payment with Stripe result
            ongoingPayment.setStatus(PaymentStatus.COMPLETED);
            ongoingPayment.setProcessedAt(LocalDateTime.now());
            ongoingPayment.setTransactionId(paymentIntent.getId());

        } catch (StripeException e) {
            log.error("Stripe payment failed: {}", e.getMessage());
            ongoingPayment.setStatus(PaymentStatus.FAILED);
        }

        return paymentRepository.save(ongoingPayment);
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
