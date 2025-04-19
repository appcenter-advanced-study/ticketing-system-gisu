package com.example.ticketing;

import com.example.ticketing.domain.reservation.ReservationRepository;
import com.example.ticketing.domain.reservation.ReservationService;
import com.example.ticketing.domain.ticket.Ticket;
import com.example.ticketing.domain.ticket.TicketRepository;
import com.example.ticketing.domain.ticketStock.TicketStock;
import com.example.ticketing.domain.ticketStock.TicketStockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ReservationServiceConcurrencyTest {

    @Autowired private ReservationService reservationService;
    @Autowired private TicketRepository ticketRepository;
    @Autowired private TicketStockRepository ticketStockRepository;
    @Autowired private ReservationRepository reservationRepository;

    private static final int THREAD_COUNT = 10;
    private static final int INITIAL_STOCK = 10;
    private Ticket ticket;

    @BeforeEach
    void setUp() {
        reservationRepository.deleteAll();
        ticketStockRepository.deleteAll();
        ticketRepository.deleteAll();

        ticket = ticketRepository.save(new Ticket("Romeo And Juliet"));
        TicketStock stock = new TicketStock(INITIAL_STOCK, ticket);
        ticketStockRepository.save(stock);
    }

    @Test
    void 동시예약요청_정합성문제_테스트() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        for (int i = 0; i < THREAD_COUNT; i++) {
            int finalI = i;
            executorService.submit(() -> {
                try {
                    reservationService.reserve(ticket.getId(), "user" + finalI);
                } catch (Exception e) {
                    // 재고 없음 등 예외 무시
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        long reservationCount = reservationRepository.count();
        int remainingStock = ticketStockRepository.findByTicketId(ticket.getId())
                .orElseThrow(() -> new RuntimeException("티켓 재고 없음"))
                .getQuantity();

        int totalUsed = (int) reservationCount + remainingStock;

        System.out.println("\n 동시성 테스트 결과");
        System.out.println("초기 티켓 수: " + INITIAL_STOCK);
        System.out.println("총 예약된 수: " + reservationCount);
        System.out.println("남은 재고: " + remainingStock);
        System.out.println("예약 + 재고 총합: " + totalUsed);

        boolean consistencyViolation = false;

        if (reservationCount == INITIAL_STOCK && remainingStock == 0) {
            System.out.println("테스트 성공: 정합성 유지 (재고 0, 예약 수 = 초기 수)");
        } else {
            System.out.println(" 동시성 문제 발생 가능성 있음");
            if (reservationCount > INITIAL_STOCK) {
                System.out.println("예약이 초과되었습니다!");
                consistencyViolation = true;
            }
            if (totalUsed != INITIAL_STOCK) {
                System.out.println("예약 + 재고 합이 초기 재고와 다릅니다!");
                consistencyViolation = true;
            }
        }

        if (consistencyViolation) {
            fail("정합성 검증 실패: 동시성 문제로 인한 데이터 불일치 발생");
        }
    }
}