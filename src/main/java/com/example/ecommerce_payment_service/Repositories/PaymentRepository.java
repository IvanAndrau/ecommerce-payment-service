package com.example.ecommerce_payment_service.Repositories;

import com.example.ecommerce_payment_service.Entities.Payment;
import com.example.ecommerce_payment_service.Entities.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Find payment by Transaction ID
    Optional<Payment> findByTransactionId(String transactionId);

    // Find payments by User ID (via Order relation)
    List<Payment> findByOrderId(long orderId);

    // Find all payments with specific status (e.g., FAILED, COMPLETED)
    List<Payment> findByStatus(PaymentStatus status);

    List<Payment> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Retrieve payments for a specific user by user ID via order
    @Query(value = "SELECT p.* FROM payments p JOIN orders o ON p.order_id = o.id WHERE o.user_id = :userId", nativeQuery = true)
    List<Payment> findByUserId(Long userId);

}
