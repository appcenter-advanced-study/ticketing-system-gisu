package com.ticketing_msa.reservation_service.service;

import com.ticketing_msa.event.dto.reservation.ReservationRequestEvent;
import com.ticketing_msa.reservation_service.config.StockClient;
import com.ticketing_msa.reservation_service.config.TicketClient;
import com.ticketing_msa.reservation_service.domain.Reservation;
import com.ticketing_msa.reservation_service.dto.request.GenerateReservationRequest;
import com.ticketing_msa.reservation_service.dto.response.ReservationResponse;
import com.ticketing_msa.reservation_service.dto.response.TicketResponse;
import com.ticketing_msa.reservation_service.kafka.ReservationEventProducer;
import com.ticketing_msa.reservation_service.repository.ReservationRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TicketClient ticketClient;
    private final StockClient stockClient;
    private final ReservationEventProducer reservationEventProducer;

    @Transactional
    public void generateReservation(GenerateReservationRequest request) {
        log.info("[예매 생성 요청] username={}, ticketId={}", request.username(), request.ticketId());
        // 1. 티켓 정보 조회 (ticket-service)
        TicketResponse ticket = ticketClient.getTicket(request.ticketId());
        if (ticket == null) {
            log.info("[예매 생성 실패] 존재하지 않는 티켓: ticketId={}", request.ticketId());
            throw new NotFoundException("티켓이 존재하지 않습니다.");
        }
        // 2. Kafka로 예매 요청 이벤트 발행 (비동기)
        ReservationRequestEvent event = ReservationRequestEvent.builder()
                .reservationId(null) // 아직 DB에 저장 전이므로 null
                .ticketId(request.ticketId())
                .username(request.username())
                .build();
        reservationEventProducer.sendReservationRequest(event);
        log.info("[예매 이벤트 발행] username={}, ticketId={}", request.username(), request.ticketId());
        // 3. (DB 저장, 재고 차감은 이벤트 소비자에서 처리)
        // 4. (필요시) 즉시 응답 반환 or 비동기 결과 polling/알림
    }

    @Transactional(readOnly = true)
    public ReservationResponse findById(Long id) {
        log.info("[예매 단건 조회] reservationId={}", id);
        Reservation reservation = reservationRepository.findById(id).orElseThrow();
        TicketResponse ticketResponse = ticketClient.getTicket(reservation.getTicketId());
        return new ReservationResponse(reservation.getId(), reservation.getUsername(), ticketResponse);
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> findAll() {
        log.info("[예매 전체 조회]");
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream().map(reservation -> {
            TicketResponse ticketResponse = ticketClient.getTicket(reservation.getTicketId());
            return new ReservationResponse(reservation.getId(), reservation.getUsername(), ticketResponse);
        }).toList();
    }

    public void deleteById(Long id) {
        log.info("[예매 삭제 요청] reservationId={}", id);
        if (!reservationRepository.existsById(id)) {
            log.warn("[예매 삭제 실패] 존재하지 않는 예약: reservationId={}", id);
            throw new RuntimeException("해당 예약이 존재하지 않습니다.");
        }
        reservationRepository.deleteById(id);
        log.info("[예매 삭제 완료] reservationId={}", id);
    }
}