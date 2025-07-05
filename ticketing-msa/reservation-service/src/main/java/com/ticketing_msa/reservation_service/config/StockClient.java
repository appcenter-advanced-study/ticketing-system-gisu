package com.ticketing_msa.reservation_service.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "stock-service")
public interface StockClient {

    @PostMapping("/api/v1/stocks/{ticketId}/decrease")
    void decreaseStock(@PathVariable Long ticketId);

    @PostMapping("/api/v1/stocks/{ticketId}/increase")
    void increaseStock(@PathVariable Long ticketId);
}
