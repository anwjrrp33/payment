package com.payment.ticket.service;

import com.payment.ticket.domain.Ticket;
import com.payment.ticket.repository.TicketLockRepository;
import com.payment.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketLockRepository ticketLockRepository;
    private final TicketRepository ticketRepository;

    // 분산 서비스인 경우 문제가 발생한다.
    public synchronized void synchronizedBuyTicketWithoutTransactional(Long ticketId) {
        Ticket ticket = ticketLockRepository.findById(ticketId) // 1
                .orElseThrow(() -> new RuntimeException(ticketId + " ticket not found"));

        ticket.decrease();

        ticketLockRepository.save(ticket);
    }

    // 분산 서비스인 경우 문제가 발생한다.
    @Transactional
    public synchronized void synchronizedBuyTicket(Long ticketId) {
        Ticket ticket = ticketLockRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException(ticketId + " ticket not found"));

        ticket.decrease();

        ticketLockRepository.saveAndFlush(ticket);
    }

    // 조회할 때 비관적 락 걸어서 분산 시스템 문제 해결
    @Transactional
    public void pessimisticBuyTicket(Long ticketId) {
        Ticket ticket = ticketLockRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException(ticketId + " ticket not found"));

        ticket.decrease();
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void buyTicketWithIsolationLevel(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException(ticketId + " ticket not found"));

        ticket.decrease();
    }
}
