package com.ticketing_msa.stock_service.service;

import com.ticketing_msa.stock_service.domain.TicketStock;
import com.ticketing_msa.stock_service.dto.response.TicketStockResponse;
import com.ticketing_msa.stock_service.repository.TicketStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketStockService {

    private final TicketStockRepository ticketStockRepository;

    public void decreaseStock(Long ticketId) {
        TicketStock stock = getStockOrThrow(ticketId);
        stock.decrease();
        ticketStockRepository.save(stock);
    }

    public void increaseStock(Long ticketId) {
        TicketStock stock = getStockOrThrow(ticketId);
        stock.increase();
        ticketStockRepository.save(stock);
    }

    public void setStockQuantity(Long ticketId, Integer quantity) {
        TicketStock stock = new TicketStock(ticketId, quantity);
        ticketStockRepository.save(stock);
    }
    public TicketStockResponse findQuantityByTicketId(Long ticketId) {
        TicketStock stock = getStockOrThrow(ticketId);
        return new TicketStockResponse(ticketId, stock.getQuantity());
    }

    private TicketStock getStockOrThrow(Long ticketId) {
        return ticketStockRepository.findByTicketId(ticketId)
                .orElseThrow(() -> new RuntimeException("티켓 재고가 존재하지 않습니다."));
    }
}
