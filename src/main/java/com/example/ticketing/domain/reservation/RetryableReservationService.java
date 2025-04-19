package com.example.ticketing.domain.reservation;

import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class RetryableReservationService {
    private final ReservationService reservationService;

    public void reserveWithRetry(Long ticketId, String username) {
        int retryCount = 3;
        while (retryCount-- > 0) {
            try {
                reservationService.reserve(ticketId, username); // 프록시 통해 트랜잭션 적용됨
                return;
            } catch (OptimisticLockException e) {
                if (retryCount == 0) {
                    throw new RuntimeException("최대 재시도 횟수 초과로 예약 실패", e);
                }
                try {
                    Thread.sleep(50 + new Random().nextInt(100));
                } catch (InterruptedException ignored) {}
            }
        }
    }
}
