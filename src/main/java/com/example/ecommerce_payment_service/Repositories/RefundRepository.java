package com.example.ecommerce_payment_service.Repositories;

import com.example.ecommerce_payment_service.Entities.Refund;
import com.example.ecommerce_payment_service.Entities.RefundStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RefundRepository extends JpaRepository<Refund, Long> {

    // Find refunds by Payment ID
    List<Refund> findByPaymentId(Long paymentId);

    // Find all refunds with a specific status
    List<Refund> findByStatus(RefundStatus status);


    // using native query to find all refunds for a specific user (via payment and order)
    @Query(value = "SELECT r.* FROM refunds r " +
            "JOIN payments p ON r.payment_id = p.id " +
            "JOIN orders o ON p.order_id = o.id " +
            "WHERE o.user_id = :userId", nativeQuery = true)
    List<Refund> findRefundsByUserId(Long userId);
}
