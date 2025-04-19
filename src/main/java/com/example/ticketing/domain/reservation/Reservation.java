package com.example.ticketing.domain.reservation;

import com.example.ticketing.domain.ticket.Ticket;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String username;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    public Reservation(String username, Ticket ticket) {
        this.username = username;
        this.ticket = ticket;
    }

}
