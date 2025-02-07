package com.example.INFO.domain.ticket.domain.repository;

import com.example.INFO.domain.ticket.domain.TicketData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketDataRepository extends JpaRepository<TicketData, String> {
}