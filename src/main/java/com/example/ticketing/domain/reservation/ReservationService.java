package com.example.ticketing.domain.reservation;

import com.example.ticketing.domain.ticket.Ticket;
import com.example.ticketing.domain.ticket.TicketRepository;
import com.example.ticketing.domain.ticketStock.TicketStock;
import com.example.ticketing.domain.ticketStock.TicketStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final TicketStockRepository ticketStockRepository;
    private final TicketRepository ticketRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public void reserve(Long ticketId, String username) {
        TicketStock stock = ticketStockRepository.findByTicketIdForUpdate(ticketId)
                .orElseThrow(() -> new RuntimeException("해당 티켓의 재고 정보가 없습니다."));

        if (stock.getQuantity() <= 0) {
            throw new RuntimeException("재고가 부족합니다.");
        }

        // 재고 감소
        stock.decreaseQuantity();

        // 예약 생성
        Ticket ticket = stock.getTicket();
        Reservation reservation = new Reservation(username, ticket);
        reservationRepository.save(reservation);
    }
}
