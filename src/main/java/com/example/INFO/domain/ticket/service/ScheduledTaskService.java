package com.example.INFO.domain.ticket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduledTaskService {

    private final TicketService ticketService;

    // 매일 00시에 갱신
    @Scheduled(cron = "0 0 0 * * *")
    public void updateWeeklyData() {
        System.out.println("Starting weekly data update...");
        ticketService.fetchAndSaveAllData();  // 매번 모든 데이터를 갱신
        System.out.println("Weekly data update completed.");
    }
}
