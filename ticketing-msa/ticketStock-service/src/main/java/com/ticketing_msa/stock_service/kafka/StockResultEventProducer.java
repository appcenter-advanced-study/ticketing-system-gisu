package com.ticketing_msa.stock_service.kafka;

import com.ticketing_msa.event.dto.stock.StockResultEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StockResultEventProducer {

    private final KafkaTemplate<String, StockResultEvent> kafkaTemplate;
    private static final String TOPIC = "stock.results";

    public void sendStockResult(StockResultEvent event) {
        log.info("[Kafka 발행] StockResultEvent: ticketId={}, username={}, success={}, message={}", event.getTicketId(), event.getUsername(), event.isSuccess(), event.getMessage());
        kafkaTemplate.send(TOPIC, event);
    }
}