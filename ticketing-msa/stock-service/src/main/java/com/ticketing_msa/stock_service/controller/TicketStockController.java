package com.ticketing_msa.stock_service.controller;

import com.ticketing_msa.stock_service.dto.request.SetStockQuantityRequest;
import com.ticketing_msa.stock_service.dto.response.TicketStockResponse;
import com.ticketing_msa.stock_service.service.TicketStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ticketStocks")
@RequiredArgsConstructor
public class TicketStockController {
    private final TicketStockService ticketStockService;

    @PostMapping("/{ticketId}/decrease")
    public ResponseEntity<Void> decreaseStock(@PathVariable Long ticketId) {
        ticketStockService.decreaseStock(ticketId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{ticketId}/increase")
    public ResponseEntity<Void> increaseStock(@PathVariable Long ticketId) {
        ticketStockService.increaseStock(ticketId);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> setStockQuantity(@RequestBody SetStockQuantityRequest request) {
        ticketStockService.setStockQuantity(request.ticketId(), request.quantity());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketStockResponse> getStockQuantity(@PathVariable Long ticketId) {
        TicketStockResponse response = ticketStockService.findQuantityByTicketId(ticketId);
        return ResponseEntity.ok(response);
    }
}
