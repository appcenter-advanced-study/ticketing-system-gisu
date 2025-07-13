package com.ticketing_msa.event.dto.reservation;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationRequestEvent {
    private Long reservationId;
    private Long ticketId;
    private String username;

}