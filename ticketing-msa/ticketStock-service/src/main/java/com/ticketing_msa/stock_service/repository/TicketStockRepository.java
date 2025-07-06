package com.ticketing_msa.stock_service.repository;

import com.ticketing_msa.stock_service.domain.TicketStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketStockRepository extends JpaRepository<TicketStock, Integer> {
    Optional<TicketStock> findByTicketId(Long ticketId);
}