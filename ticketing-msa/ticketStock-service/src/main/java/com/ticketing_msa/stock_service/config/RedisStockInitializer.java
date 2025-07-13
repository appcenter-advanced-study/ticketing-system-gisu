package com.ticketing_msa.stock_service.config;


import com.ticketing_msa.stock_service.domain.TicketStock;
import com.ticketing_msa.stock_service.repository.TicketStockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisStockInitializer {

    private final TicketStockRepository ticketStockRepository;
    private final StringRedisTemplate redisTemplate;

    /**
     * 서비스 시작 시 DB 재고를 Redis에 초기화
     */
    @PostConstruct
    public void preloadStockToRedis() {
        List<TicketStock> stockList = ticketStockRepository.findAll();
        for (TicketStock stock : stockList) {
            String redisKey = "stock:" + stock.getTicketId();
            boolean inserted = Boolean.TRUE.equals(redisTemplate.opsForValue()
                    .setIfAbsent(redisKey, String.valueOf(stock.getQuantity())));
            if (inserted) {
                log.info("[Redis 재고 초기화] {} -> {}", redisKey, stock.getQuantity());
            } else {
                log.info("[이미 존재하는 재고 키] {} - 초기화 생략", redisKey);
            }
        }
    }
}
