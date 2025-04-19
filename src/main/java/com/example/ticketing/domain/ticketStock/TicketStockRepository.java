package com.example.ticketing.domain.ticketStock;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TicketStockRepository extends JpaRepository<TicketStock, Integer> {
     @Lock(LockModeType.PESSIMISTIC_WRITE)
     @Query("SELECT ts FROM TicketStock ts WHERE ts.ticket.id = :ticketId")
     Optional<TicketStock> findByTicketIdForUpdate(@Param("ticketId") Long ticketId);

     Optional<TicketStock> findByTicketId(Long ticketId);
}
