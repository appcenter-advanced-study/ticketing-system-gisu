package com.ticketing_msa.reservation_service.service;

import com.ticketing_msa.reservation_service.config.StockClient;
import com.ticketing_msa.reservation_service.config.TicketClient;
import com.ticketing_msa.reservation_service.domain.Reservation;
import com.ticketing_msa.reservation_service.dto.request.GenerateReservationRequest;
import com.ticketing_msa.reservation_service.dto.response.ReservationResponse;
import com.ticketing_msa.reservation_service.dto.response.TicketResponse;
import com.ticketing_msa.reservation_service.repository.ReservationRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TicketClient ticketClient;
    private final StockClient stockClient;

    @Transactional
    public void generateReservation(GenerateReservationRequest request) {

        // 1. 티켓 정보 조회 (ticket-service)
        TicketResponse ticket = ticketClient.getTicket(request.ticketId());
        if (ticket == null) {
            throw new NotFoundException("티켓이 존재하지 않습니다.");
        }

        // 2. 재고 차감 (ticketStock-service)
        stockClient.decreaseStock(request.ticketId());

        // 3. 예약 정보 저장 (reservation-service)
        Reservation reservation = new Reservation(request.username(), request.ticketId());
        reservationRepository.save(reservation);

        // 4. (필요시) 응답 반환
    }

    @Transactional(readOnly = true)
    public ReservationResponse findById(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow();
        TicketResponse ticketResponse = ticketClient.getTicket(reservation.getTicketId());
        return new ReservationResponse(reservation.getId(), reservation.getUsername(), ticketResponse);
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> findAll() {
        List<Reservation> reservations = reservationRepository.findAll();

        return reservations.stream().map(reservation -> {
            TicketResponse ticketResponse = ticketClient.getTicket(reservation.getTicketId());
            return new ReservationResponse(reservation.getId(), reservation.getUsername(), ticketResponse);
        }).toList();
    }

    public void deleteById(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new RuntimeException("해당 예약이 존재하지 않습니다.");
        }
        reservationRepository.deleteById(id);
    }
}