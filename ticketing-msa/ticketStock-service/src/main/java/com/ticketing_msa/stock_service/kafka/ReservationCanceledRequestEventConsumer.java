package com.ticketing_msa.stock_service.kafka;

import com.ticketing_msa.event.dto.reservation.ReservationCanceledRequestEvent;
import com.ticketing_msa.stock_service.service.TicketStockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationCanceledRequestEventConsumer {

    private final TicketStockService ticketStockService;

    @KafkaListener(topics = "reservation.requests-canceled", groupId = "stock-service")
    public void consume(ReservationCanceledRequestEvent event) {
        log.info("[Kafka 수신] ReservationCanceledRequestEvent: ticketId={}, username={}", event.getTicketId(), event.getUsername());
        // Redis/DB 재고 증가
        ticketStockService.handleReservationCancel(event);
    }
}
