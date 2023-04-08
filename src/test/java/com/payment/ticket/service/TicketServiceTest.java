package com.payment.ticket.service;

import com.payment.ticket.domain.Stock;
import com.payment.ticket.domain.Ticket;
import com.payment.ticket.repository.TicketRepository;
import com.payment.ticket.utils.DatabaseUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class TicketServiceTest {

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private DatabaseUtils databaseUtils;

    @BeforeEach
    public void init() {
        databaseUtils.afterPropertiesSet();
        databaseUtils.execute();
    }

    @DisplayName("synchronized를 사용한 동시성 처리")
    @Test
    void synchronizedBuyTicketWithoutTransactionalTest() throws InterruptedException {
        //given
        AtomicInteger requestSuccessCount = new AtomicInteger(0);
        AtomicInteger requestFailCount = new AtomicInteger(0);

        Ticket ticket = new Ticket("springCamp2020", new Stock(100));
        ticketRepository.save(ticket);
        StopWatch stopWatch = new StopWatch();

        //when
        stopWatch.start();
        CountDownLatch countDownLatch = new CountDownLatch(1000);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 1000; i++) {
            executorService.execute(
                    () -> {
                        try {
                            ticketService.synchronizedBuyTicketWithoutTransactional(1L);
                            requestSuccessCount.getAndIncrement();
                        } catch (Exception e) {
                            requestFailCount.getAndIncrement();
                        } finally {
                            countDownLatch.countDown();
                        }
                    }
            );
        }
        countDownLatch.await();
        System.out.println(stopWatch.getLastTaskTimeMillis());

        //then
        extracted(requestSuccessCount, requestFailCount);
    }

    @DisplayName("transaction과 synchronized를 사용한 동시성 처리")
    @Test
    void synchronizedBuyTicketTest() throws InterruptedException {
        //given
        AtomicInteger requestSuccessCount = new AtomicInteger(0);
        AtomicInteger requestFailCount = new AtomicInteger(0);

        Ticket ticket = new Ticket("springCamp2020", new Stock(100));
        ticketRepository.save(ticket);
        StopWatch stopWatch = new StopWatch();

        //when
        stopWatch.start();
        CountDownLatch countDownLatch = new CountDownLatch(1000);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 1000; i++) {
            executorService.execute(
                    () -> {
                        try {
                            ticketService.synchronizedBuyTicket(1L);
                            requestSuccessCount.getAndIncrement();
                        } catch (Exception e) {
                            requestFailCount.getAndIncrement();
                        } finally {
                            countDownLatch.countDown();
                        }
                    }
            );
        }
        countDownLatch.await();
        System.out.println(stopWatch.getLastTaskTimeMillis());

        //then
        extracted(requestSuccessCount, requestFailCount);
    }

    @DisplayName("비관적 락을 사용한 동시성 처리")
    @Test
    void pessimisticBuyTicketTest() throws InterruptedException {
        //given
        AtomicInteger requestSuccessCount = new AtomicInteger(0);
        AtomicInteger requestFailCount = new AtomicInteger(0);

        Ticket ticket = new Ticket("springCamp2020", new Stock(100));
        ticketRepository.save(ticket);
        StopWatch stopWatch = new StopWatch();

        //when
        stopWatch.start();
        CountDownLatch countDownLatch = new CountDownLatch(1000);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 1000; i++) {
            executorService.execute(
                    () -> {
                        try {
                            ticketService.pessimisticBuyTicket(1L);
                            requestSuccessCount.getAndIncrement();
                        } catch (Exception e) {
                            requestFailCount.getAndIncrement();
                        } finally {
                            countDownLatch.countDown();
                        }
                    }
            );
        }
        countDownLatch.await();
        System.out.println(stopWatch.getLastTaskTimeMillis());

        //then
        extracted(requestSuccessCount, requestFailCount);
    }

    @DisplayName("비관적락과 격리레벨 SERIALIZABLE 사용한 동시성 처리")
    @Test
    void buyTicketWithIsolationLevelTest() throws InterruptedException {
        //given
        AtomicInteger requestSuccessCount = new AtomicInteger(0);
        AtomicInteger requestFailCount = new AtomicInteger(0);

        Ticket ticket = new Ticket("springCamp2020", new Stock(100));
        ticketRepository.save(ticket);
        StopWatch stopWatch = new StopWatch();

        //when
        stopWatch.start();
        CountDownLatch countDownLatch = new CountDownLatch(1000);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 1000; i++) {
            executorService.execute(
                    () -> {
                        try {
                            ticketService.buyTicketWithIsolationLevel(1L);
                            requestSuccessCount.getAndIncrement();
                        } catch (Exception e) {
                            requestFailCount.getAndIncrement();
                        } finally {
                            countDownLatch.countDown();
                        }
                    }
            );
        }
        countDownLatch.await();
        System.out.println(stopWatch.getLastTaskTimeMillis());

        //then
        extracted(requestSuccessCount, requestFailCount);
    }

    private static void extracted(AtomicInteger requestSuccessCount, AtomicInteger requestFailCount) {
        assertAll(
                () -> assertThat(requestSuccessCount.get()).isEqualTo(100),
                () -> assertThat(requestFailCount.get()).isEqualTo(900)
        );
    }
}
