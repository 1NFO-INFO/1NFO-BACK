package com.example.INFO.domain.ticket.controller;

import com.example.INFO.domain.ticket.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ticket")
@Tag(name = "티켓 API", description = "티켓 관련 API")
public class TicketController {
    private final TicketService ticketService;

    // 매주 월요일 오전 2시에 데이터 갱신
    @Operation(summary = "모든 데이터 갱신", description = "API를 호출하면 모든 데이터를 갱신하여 저장")
    @ApiResponse(responseCode = "200", description = "데이터 갱신 성공")
    @GetMapping("/fetch-all-data")
    public String fetchAllData() {
        ticketService.fetchAndSaveAllData();
        return "All data fetched and saved successfully!";
    }
}