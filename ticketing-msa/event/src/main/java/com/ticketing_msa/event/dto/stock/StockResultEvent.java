package com.ticketing_msa.event.dto.stock;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockResultEvent {
    private Long reservationId;
    private Long ticketId;
    private String username;
    private boolean success; // true: 성공, false: 실패
    private String message;
}