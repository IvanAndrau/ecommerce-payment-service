package com.example.ecommerce_payment_service.Services;

import com.example.ecommerce_payment_service.Entities.Payment;
import com.example.ecommerce_payment_service.Entities.PaymentStatus;
import com.example.ecommerce_payment_service.Repositories.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PaymentServiceImpl implements IPaymentService{

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    // Creates a new Payment object with provided properties
    @Override
    public Payment createPayment(Long orderId, BigDecimal amount, String transactionId) {

        log.info("Payment object is being created");
        Payment payment = new Payment();

        try {
            payment.setOrderId(orderId);
            payment.setAmount(amount);
            payment.setStatus(PaymentStatus.INITIATED);
            payment.setTransactionId(transactionId);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return paymentRepository.save(payment);
    }

    // Process the payment and interact with third-parties gateways
    @Override
    public Payment processPayment(Payment payment)
    {
        // imitation of status after interacting with third-party gateway
        boolean thirdPartyServiceAcceptedPayment;
        Payment ongoingPayment = new Payment();

        try {
            ongoingPayment = paymentRepository.getReferenceById(payment.getId());
            // Place to insert third-party gateway to handle payment
            thirdPartyServiceAcceptedPayment = true;

            if(thirdPartyServiceAcceptedPayment) {
                ongoingPayment.setStatus(PaymentStatus.COMPLETED);
            }
            else {  // if third-party gateway rejected the payment
                ongoingPayment.setStatus(PaymentStatus.CANCELLED);
            }
        }
        catch(Exception e) {
            ongoingPayment.setStatus(PaymentStatus.FAILED);
            e.printStackTrace();    //
        }

        return ongoingPayment;
    }

    // Retrieves details of a specific payment
    @Override
    public Payment getPaymentById(Long paymentId)
    {
        Payment payment = new Payment();

        try {
            payment = paymentRepository.getReferenceById(paymentId);
        }
        catch(Exception e) {
            e.printStackTrace();    //
        }

        return payment;
    }

    // Retrieves a list of payments for a specific user
    @Override
    public List<Payment> getPaymentsByUserId(Long userId)
    {
        List<Payment> payments = new ArrayList<>();

        try {
            payments = paymentRepository.findByUserId(userId);
        }
        catch(Exception e) {
            e.printStackTrace();    //
        }

        return payments;
    }

    //Fetch payment details related to a specific order
    @Override
    public List<Payment> getPaymentsByOrderId(Long orderId)
    {
        List<Payment> payments = new ArrayList<>();

        try {
            payments = paymentRepository.findByOrderId(orderId);
        }
        catch(Exception e) {
            e.printStackTrace();    //
        }

        return payments;
    }
}
