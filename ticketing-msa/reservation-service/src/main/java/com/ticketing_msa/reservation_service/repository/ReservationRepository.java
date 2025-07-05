package com.ticketing_msa.reservation_service.repository;

import com.ticketing_msa.reservation_service.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
