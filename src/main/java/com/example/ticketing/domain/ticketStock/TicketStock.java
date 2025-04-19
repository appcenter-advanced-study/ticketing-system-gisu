package com.example.ticketing.domain.ticketStock;


import com.example.ticketing.domain.ticket.Ticket;
import com.example.ticketing.global.BaseEntity;
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
public class TicketStock extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantity;

    @Version
    private Long version;

    @OneToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;


    public TicketStock(Integer quantity, Ticket ticket) {
        this.quantity = quantity;
        this.ticket = ticket;
    }

    public void decreaseQuantity() {
        this.quantity = this.quantity - 1;
    }

}
