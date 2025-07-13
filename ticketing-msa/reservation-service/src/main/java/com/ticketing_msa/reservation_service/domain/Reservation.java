package com.ticketing_msa.reservation_service.domain;


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

    private Long ticketId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;


    public Reservation(String username, Long ticketId) {
        this.username = username;
        this.ticketId = ticketId;
        this.status = ReservationStatus.PENDING; // 기본 상태
    }

    public void updateReservation(String username, Long ticketId) {
        this.username = username != null ? username : this.username;
        this.ticketId = ticketId != null ? ticketId : this.ticketId;
    }

    public void cancel() {
        this.status = ReservationStatus.CANCELED;
    }

    public void confirm() {
        this.status = ReservationStatus.CONFIRMED;
    }


}

