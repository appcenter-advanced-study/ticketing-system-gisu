package com.ticketing_msa.reservation_service.dto.response;

import com.ticketing_msa.reservation_service.domain.Reservation;

import java.time.LocalDateTime;

public record ReservationResponse(
        Long id,
        String username,
        TicketResponse ticket
) {
}
