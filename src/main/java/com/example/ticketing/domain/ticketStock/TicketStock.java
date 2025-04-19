package com.example.ticketing.domain.ticketStock;


import com.example.ticketing.domain.ticket.Ticket;
import com.example.ticketing.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TicketStock")
public class TicketStock extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantity;

    @OneToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;


    public TicketStock(Integer quantity, Ticket ticket) {
        this.quantity = quantity;
        this.ticket = ticket;
    }

    public void decreaseQuantity() {
        System.out.println("📉 재고 감소 전: " + this.quantity);
        this.quantity = this.quantity - 1;
        System.out.println("📉 재고 감소 후: " + this.quantity);
    }

}
