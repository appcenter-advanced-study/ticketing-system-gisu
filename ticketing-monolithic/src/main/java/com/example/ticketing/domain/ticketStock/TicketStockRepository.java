package com.example.ticketing.domain.ticketStock;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketStockRepository extends JpaRepository<TicketStock, Integer> {
     Optional<TicketStock> findByTicketId(Long ticketId);
}
