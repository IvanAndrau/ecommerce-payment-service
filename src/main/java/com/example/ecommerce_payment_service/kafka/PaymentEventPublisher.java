package com.example.ecommerce_payment_service.kafka;

import com.example.ecommerce_payment_service.dto.PaymentConfirmedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentEventPublisher {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void sendPaymentConfirmedEvent(Long orderId, String transactionId) {
        String topic = "payment-confirmed";
        try {
            String message = objectMapper.writeValueAsString(new PaymentConfirmedEvent(orderId, "PAID"));
            kafkaTemplate.send(topic, message);
            log.info("Sent payment-confirmed event: {}", message);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize PaymentConfirmedEvent: {}", e.getMessage());
        }
    }
}
