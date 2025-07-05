package com.ticketing_msa.reservation_service.service;

import com.ticketing_msa.reservation_service.config.TicketClient;
import com.ticketing_msa.reservation_service.domain.Reservation;
import com.ticketing_msa.reservation_service.dto.request.GenerateReservationRequest;
import com.ticketing_msa.reservation_service.dto.response.ReservationResponse;
import com.ticketing_msa.reservation_service.dto.response.TicketResponse;
import com.ticketing_msa.reservation_service.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TicketClient ticketClient;

    public void generateReservation(GenerateReservationRequest request) {
        Reservation reservation = new Reservation(request.username(), request.ticketId());
        reservationRepository.save(reservation);
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