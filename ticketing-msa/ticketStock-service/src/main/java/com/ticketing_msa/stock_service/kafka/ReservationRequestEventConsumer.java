package com.ticketing_msa.stock_service.kafka;

import com.ticketing_msa.event.dto.reservation.ReservationRequestEvent;
import com.ticketing_msa.event.dto.stock.StockResultEvent;
import com.ticketing_msa.stock_service.service.TicketStockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationRequestEventConsumer {

    private final TicketStockService stockService;

    @KafkaListener(topics = "reservation.requests.register", groupId = "stock-service")
    public void consume(ReservationRequestEvent event) {
        log.info("[Kafka 수신] ReservationRequestEvent: ticketId={}, username={}", event.getTicketId(), event.getUsername());
        stockService.handleReservationRequest(event);
    }
}