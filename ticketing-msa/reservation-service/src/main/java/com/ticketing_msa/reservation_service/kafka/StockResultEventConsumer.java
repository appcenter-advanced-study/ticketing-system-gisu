package com.ticketing_msa.reservation_service.kafka;

import com.ticketing_msa.event.dto.stock.StockResultEvent;
import com.ticketing_msa.reservation_service.domain.Reservation;
import com.ticketing_msa.reservation_service.repository.ReservationRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class StockResultEventConsumer {

    private final ReservationRepository reservationRepository;

    @KafkaListener(topics = "stock.results", groupId = "reservation-group")
    public void handleStockResult(StockResultEvent event) {
        if (event.isSuccess()) {
            // 예매 성공: 예약 정보 저장
            Reservation reservation = new Reservation(event.getUsername(), event.getTicketId());
            reservationRepository.save(reservation);
            log.info("[Kafka 수신- 예매 성공] topic=stock.results, reservationId={}, success={}", event.getReservationId(), event.isSuccess());
        } else if ("CANCEL_OK".equals(event.getMessage())) {
            // 예매 취소: 상태를 CANCELED로 변경
            Reservation reservation = reservationRepository.findById(event.getReservationId())
                    .orElseThrow(() -> new NotFoundException("예약 없음"));
            reservation.cancel();
            reservationRepository.save(reservation);
            log.info("[Kafka 수신- 예매 취소] topic=stock.results, reservationId={}, success={}", event.getReservationId(), event.isSuccess());
        } else {
            // 예매 실패 처리
            log.warn("예매 실패: ticketId={}, username={}, reason={}", event.getTicketId(), event.getUsername(), event.getMessage());
            // 실패 이력 저장 (예: Reservation 엔티티에 FAILED 상태로 저장)
        }
    }
}