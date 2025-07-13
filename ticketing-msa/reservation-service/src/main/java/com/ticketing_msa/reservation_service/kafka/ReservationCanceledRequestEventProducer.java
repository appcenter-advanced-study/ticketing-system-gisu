package com.ticketing_msa.reservation_service.kafka;

import com.ticketing_msa.event.dto.reservation.ReservationCanceledRequestEvent;
import com.ticketing_msa.event.dto.reservation.ReservationRequestEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationCanceledRequestEventProducer {
    private final KafkaTemplate<String, ReservationCanceledRequestEvent> kafkaTemplate;

    private static final String TOPIC = "reservation.requests.canceled";

    public void sendReservationCanceled(ReservationCanceledRequestEvent event) {
        kafkaTemplate.send(TOPIC, event);
    }
}
