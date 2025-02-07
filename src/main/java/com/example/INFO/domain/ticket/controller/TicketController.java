package com.example.INFO.domain.ticket.controller;

import com.example.INFO.domain.ticket.dto.req.TicketAreasFilterRequest;
import com.example.INFO.domain.ticket.dto.req.TicketPlaceFilterRequest;
import com.example.INFO.domain.ticket.dto.res.TicketResponse;
import com.example.INFO.domain.ticket.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    //최신순 정렬
    @Operation(summary = "최신 시작일 순 정렬", description = "공연 시작일이 최신 순서대로 정렬 후 반환")
    @ApiResponse(responseCode = "200", description = "정렬된 티켓 목록",
            content = @Content(schema = @Schema(implementation = TicketResponse.class)))
    @GetMapping("/start-date-desc")
    public List<TicketResponse> getSortedByStartDateDesc() {
        return ticketService.getSortedByStartDateDesc();
    }

    // 마감 임박순 정렬
    @Operation(summary = "마감 임박 순 정렬", description = "마감일이 가까운 순서대로 티켓을 정렬 후 반환")
    @ApiResponse(responseCode = "200", description = "정렬된 티켓 목록",
            content = @Content(schema = @Schema(implementation = TicketResponse.class)))
    @GetMapping("/end-date-soon")
    public List<TicketResponse> getSortedByEndDate() {
        return ticketService.getSortedByEndDateClosestToToday();
    }
    //  공연장 필터링
    @Operation(summary = "공연장 필터링", description = "입력한 공연장 리스트에 해당하는 티켓을 반환")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "필터링된 티켓 목록",
                    content = @Content(schema = @Schema(implementation = TicketResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping("/filter-places")
    public List<TicketResponse> filterByPlaces(@RequestBody TicketPlaceFilterRequest request) {
        return ticketService.filterByPlaces(request.getPlaces());
    }

    // 지역 필터링
    @Operation(summary = "지역 필터링", description = "입력한 지역 리스트에 해당하는 티켓을 반환")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "필터링된 티켓 목록",
                    content = @Content(schema = @Schema(implementation = TicketResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping("/filter-areas")
    public List<TicketResponse> filterByAreas(@RequestBody TicketAreasFilterRequest request) {
        return ticketService.filterByAreas(request.getAreas());
    }

    //티켓 상세 조회
    @Operation(summary = "티켓 상세 조회", description = "특정 티켓 ID로 티켓 상세 정보를 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "티켓 상세 정보",
                    content = @Content(schema = @Schema(implementation = TicketResponse.class))),
            @ApiResponse(responseCode = "404", description = "해당 티켓을 찾을 수 없음")
    })
    @GetMapping("/search/{id}")
    public ResponseEntity<TicketResponse> getTicketDetail(@PathVariable String id) {
        return ticketService.getTicketDetail(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}