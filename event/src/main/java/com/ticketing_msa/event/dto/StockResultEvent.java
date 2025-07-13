package com.ticketing_msa.event.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockResultEvent {
    private Long reservationId;
    private Long ticketId;
    private Long userId;
    private int quantity;
    private boolean success; // true: 성공, false: 실패
    private String reason;   // 실패 사유(OUT_OF_STOCK 등)
    private String requestId; // 트래킹용(옵션)
} 