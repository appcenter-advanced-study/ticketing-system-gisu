package com.ticketing_msa.stock_service.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TicketStock")
@Slf4j
public class TicketStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantity;

    private Long ticketId;

    public TicketStock( Long ticketId, Integer quantity) {
        this.ticketId = ticketId;
        this.quantity = quantity;
    }

    public void decrease() {
        if (this.quantity <= 0) {
            throw new IllegalStateException("No more tickets available");
        } else {
            this.quantity--;
        }
    }

    public void increase() {
        this.quantity++;
    }

}

