package com.example.ecommerce_payment_service.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    @Size(max = 50)
    private String status;

    @Column(nullable = true)
    @Size(max = 100)
    private String transactionId;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;    //private Timestamp createdAt;
}