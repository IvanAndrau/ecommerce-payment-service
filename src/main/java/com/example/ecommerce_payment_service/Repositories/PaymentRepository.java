package com.example.ecommerce_payment_service.Repositories;

import com.example.ecommerce_payment_service.Entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

}
