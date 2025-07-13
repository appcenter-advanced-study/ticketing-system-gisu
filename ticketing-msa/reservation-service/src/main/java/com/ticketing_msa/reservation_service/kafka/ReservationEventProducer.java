package com.ticketing_msa.reservation_service.kafka;

import com.ticketing_msa.event.dto.reservation.ReservationCanceledRequestEvent;
import com.ticketing_msa.event.dto.reservation.ReservationRequestEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationEventProducer {

    private final KafkaTemplate<String, ReservationRequestEvent> kafkaTemplate;

    private static final String TOPIC = "reservation.requests.register";

    public void sendReservationRequest(ReservationRequestEvent event) {
        kafkaTemplate.send(TOPIC, event);
    }


}