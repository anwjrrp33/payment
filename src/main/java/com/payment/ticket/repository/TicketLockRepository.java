package com.payment.ticket.repository;

import com.payment.ticket.domain.Ticket;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface TicketLockRepository extends JpaRepository<Ticket, Long> {

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    Optional<Ticket> findById(Long id);
}
