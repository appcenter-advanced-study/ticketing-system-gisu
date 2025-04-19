package com.example.ticketing.domain.reservation;

import com.example.ticketing.domain.ticket.Ticket;
import com.example.ticketing.domain.ticket.TicketRepository;
import com.example.ticketing.domain.ticketStock.TicketStock;
import com.example.ticketing.domain.ticketStock.TicketStockRepository;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final TicketStockRepository ticketStockRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public void reserve(Long ticketId, String username) {
        TicketStock stock = ticketStockRepository.findByTicketId(ticketId)
                .orElseThrow(() -> new RuntimeException("해당 티켓의 재고 정보가 없습니다."));

        stock.decreaseQuantity();

        Reservation reservation = new Reservation(username, stock.getTicket());
        reservationRepository.save(reservation);
    }

}
