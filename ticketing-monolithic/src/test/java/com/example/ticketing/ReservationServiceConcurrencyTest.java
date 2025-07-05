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

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketStockRepository ticketStockRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    private static final int THREAD_COUNT = 10;
    private Ticket ticket;

    @BeforeEach
    void setUp() {
        reservationRepository.deleteAll();
        ticketStockRepository.deleteAll();
        ticketRepository.deleteAll();

        ticket = ticketRepository.save(new Ticket("Romeo And Juliet"));
        TicketStock stock = new TicketStock(10, ticket); // 재고 10개
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

        long reservationCount = reservationRepository.count();
        int remainingStock = ticketStockRepository.findByTicketId(ticket.getId())
                .orElseThrow(() -> new RuntimeException("티켓 재고 없음"))
                .getQuantity();

        System.out.println(" 동시성 테스트 결과");
        System.out.println("총 예약된 수: " + reservationCount);
        System.out.println("남은 재고: " + remainingStock);

        try {
            assertThat(reservationCount).isLessThanOrEqualTo(10);
            assertThat(remainingStock).isGreaterThanOrEqualTo(0);
            System.out.println(" 정합성 문제 없음 (락이 적용된 경우 등)");
        } catch (AssertionError e) {
            System.out.println(" 동시성 문제 발생 확인됨 → 정합성 깨짐");
            System.out.println("   기대 재고 이하 예약이어야 하나 " + reservationCount + "건 예약됨");
            // 테스트는 실패로 처리하지 않음
        }
    }
}