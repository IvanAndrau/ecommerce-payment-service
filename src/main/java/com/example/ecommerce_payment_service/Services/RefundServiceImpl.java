package com.example.ecommerce_payment_service.Services;

import com.example.ecommerce_payment_service.Entities.Payment;
import com.example.ecommerce_payment_service.Entities.PaymentStatus;
import com.example.ecommerce_payment_service.Entities.Refund;
import com.example.ecommerce_payment_service.Entities.RefundStatus;
import com.example.ecommerce_payment_service.Repositories.PaymentRepository;
import com.example.ecommerce_payment_service.Repositories.RefundRepository;
import com.example.ecommerce_payment_service.Services.IRefundService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RefundServiceImpl implements IRefundService {

    private final RefundRepository refundRepository;
    private final PaymentRepository paymentRepository;

    public RefundServiceImpl(RefundRepository refundRepository, PaymentRepository paymentRepository) {
        this.refundRepository = refundRepository;
        this.paymentRepository = paymentRepository;
    }

//    @Override
//    @Transactional
//    // Start a refund process for a completed Payment
//    public Refund initiateRefund(Long paymentId) {
//        Payment payment = paymentRepository.findById(paymentId).orElse(null);
//        Refund refund = new Refund();
//        if(payment == null) {
//            refund.setRefundAmount(0.0);
//            refund.setStatus(RefundStatus.FAILED);
//            refund.setReason("");   //should we get a refund object from database or should we create it inside this method?
//        }
//        return refund;  //not sure
//    }

    @Override
    @Transactional
    public Refund initiateRefund(Long paymentId, Double refundAmount) {
        Optional<Payment> paymentOpt = paymentRepository.findById(paymentId);

        Payment payment = paymentOpt.get();
        if (refundAmount <= 0) {
            log.error("Refund amount is less than or equal to 0");
            throw new IllegalArgumentException("Refund amount is less than or equal to 0");
        }

        Refund refund = new Refund();
        refund.setPayment(payment);
        refund.setRefundAmount(refundAmount);
        refund.setStatus(RefundStatus.PENDING);

        return refundRepository.save(refund);
    }

    @Override
    public Refund getRefundStatus(Long refundId) {
        return refundRepository.findById(refundId)
                .orElseThrow(() -> new IllegalArgumentException("Refund not found"));
    }

    @Override
    @Transactional
    public Payment cancelPayment(Long paymentId) {
        Optional<Payment> paymentOpt = paymentRepository.findById(paymentId);
        if (paymentOpt.isEmpty()) {
            throw new IllegalArgumentException("Payment not found");
        }
        else if(paymentOpt.get().getStatus() != PaymentStatus.INITIATED)
        {
            throw new IllegalArgumentException("Payment is not initiated");
        }

        Payment payment = paymentOpt.get();
        payment.setStatus(PaymentStatus.CANCELLED);
        return paymentRepository.save(payment);
    }

    @Override
    public List<Payment> getFailedPayments() {
        return paymentRepository.findByStatus(PaymentStatus.FAILED);
    }
}