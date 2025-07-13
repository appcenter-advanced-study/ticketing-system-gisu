package com.ticketing_msa.stock_service.repository;

import com.ticketing_msa.stock_service.domain.TicketStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface TicketStockRepository extends JpaRepository<TicketStock, Integer> {
    Optional<TicketStock> findByTicketId(Long ticketId);

    @Modifying
    @Transactional
    @Query("UPDATE TicketStock t SET t.quantity = t.quantity - :quantity WHERE t.ticketId = :ticketId AND t.quantity >= :quantity")
    int decreaseStock(@Param("ticketId") Long ticketId, @Param("quantity") int quantity);
}