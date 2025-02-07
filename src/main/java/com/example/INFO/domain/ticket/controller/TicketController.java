package com.example.INFO.domain.ticket.controller;

import com.example.INFO.domain.ticket.dto.res.TicketResponse;
import com.example.INFO.domain.ticket.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    // 높은 할인율순 정렬
    @Operation(summary = "할인율 높은 순 정렬", description = "할인율이 높은 순서대로 티켓을 정렬 후 반환")
    @ApiResponse(responseCode = "200", description = "정렬된 티켓 목록",
            content = @Content(schema = @Schema(implementation = TicketResponse.class)))
    @GetMapping("/discount-rate-desc")
    public List<TicketResponse> getSortedByDiscountRateDesc() {
        return ticketService.getSortedByDiscountRateDesc();
    }
}