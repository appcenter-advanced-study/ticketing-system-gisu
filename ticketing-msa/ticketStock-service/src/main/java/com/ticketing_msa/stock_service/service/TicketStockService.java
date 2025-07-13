package com.ticketing_msa.stock_service.service;

import com.ticketing_msa.event.dto.reservation.ReservationCanceledRequestEvent;
import com.ticketing_msa.event.dto.reservation.ReservationRequestEvent;
import com.ticketing_msa.event.dto.stock.StockResultEvent;
import com.ticketing_msa.stock_service.domain.TicketStock;
import com.ticketing_msa.stock_service.dto.response.TicketStockResponse;
import com.ticketing_msa.stock_service.kafka.StockResultEventProducer;
import com.ticketing_msa.stock_service.repository.TicketStockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketStockService {

    private final TicketStockRepository ticketStockRepository;
    private final StringRedisTemplate redisTemplate;
    private final StockResultEventProducer stockResultEventProducer;

    public void handleReservationRequest(ReservationRequestEvent event) {
        log.info("[예매 요청 이벤트 수신] ticketId={}, username={}", event.getTicketId(), event.getUsername());
        String stockKey = "stock:" + event.getTicketId();

        Long stock = redisTemplate.opsForValue().decrement(stockKey);
        log.info("[Redis 재고 차감] ticketId={}, 남은 재고={}", event.getTicketId(), stock);

        boolean success = stock != null && stock >= 0;
        String reason = success ? null : "OUT_OF_STOCK";
        if (success) {
            int updated = ticketStockRepository.decreaseStock(event.getTicketId(), 1);
            log.info("[DB 재고 차감] ticketId={}, 차감 결과(updated)={}", event.getTicketId(), updated);
            if (updated == 0) {
                redisTemplate.opsForValue().increment(stockKey);
                success = false;
                reason = "OUT_OF_STOCK_DB";
                log.warn("[DB 재고 차감 실패] ticketId={}, Redis 롤백 수행", event.getTicketId());
            }
        } else {
            log.warn("[Redis 재고 부족] ticketId={}, username={}", event.getTicketId(), event.getUsername());
        }
        StockResultEvent resultEvent = StockResultEvent.builder()
            .reservationId(event.getReservationId())
            .ticketId(event.getTicketId())
            .username(event.getUsername())
            .success(success)
            .message(reason)
            .build();

        stockResultEventProducer.sendStockResult(resultEvent);
        log.info("[재고 결과 이벤트 발행] ticketId={}, username={}, success={}, reason={}", event.getTicketId(), event.getUsername(), success, reason);
    }


    public void handleReservationCancel(ReservationCanceledRequestEvent event) {
        log.info("[예매 취소 이벤트 수신] ticketId={}, username={}", event.getTicketId(), event.getUsername());
        String stockKey = "stock:" + event.getTicketId();
        redisTemplate.opsForValue().increment(stockKey);
        // DB 재고 증가
        increaseStock(event.getTicketId());
        // 결과 이벤트 발행
        StockResultEvent resultEvent = StockResultEvent.builder()
                .reservationId(event.getReservationId())
                .ticketId(event.getTicketId())
                .username(event.getUsername())
                .success(true)
                .message("CANCEL_OK")
                .build();
        stockResultEventProducer.sendStockResult(resultEvent);
    }


    public void decreaseStock(Long ticketId) {
        log.info("[DB 재고 차감] ticketId={}", ticketId);
        TicketStock stock = getStockOrThrow(ticketId);
        stock.decrease();
        ticketStockRepository.save(stock);
    }

    public void increaseStock(Long ticketId) {
        log.info("[DB 재고 증가] ticketId={}", ticketId);
        TicketStock stock = getStockOrThrow(ticketId);
        stock.increase();
        ticketStockRepository.save(stock);
    }

    public void setStockQuantity(Long ticketId, Integer quantity) {
        log.info("[재고 수량 세팅] ticketId={}, quantity={}", ticketId, quantity);
        TicketStock stock = new TicketStock(ticketId, quantity);
        ticketStockRepository.save(stock);


        // Redis 등록
        String redisKey = "stock:" + ticketId;
        redisTemplate.opsForValue().set(redisKey, String.valueOf(quantity));
        log.info("[Redis 재고 등록] key={}, quantity={}", redisKey, quantity);
    }

    public TicketStockResponse findQuantityByTicketId(Long ticketId) {
        log.info("[재고 조회] ticketId={}", ticketId);
        TicketStock stock = getStockOrThrow(ticketId);
        return new TicketStockResponse(ticketId, stock.getQuantity());
    }

    private TicketStock getStockOrThrow(Long ticketId) {
        return ticketStockRepository.findByTicketId(ticketId)
                .orElseThrow(() -> {
                    log.warn("[재고 조회 실패] ticketId={}", ticketId);
                    return new RuntimeException("티켓 재고가 존재하지 않습니다.");
                });
    }
} 