package com.ticketing_msa.event.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationRequestEvent {
    private Long reservationId;
    private Long ticketId;
    private Long userId;
    private int quantity;
    private String requestId; // 트래킹용(옵션)
} 