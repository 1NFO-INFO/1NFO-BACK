package com.example.INFO.domain.favorite.controller;

import com.example.INFO.domain.favorite.dto.res.TicketDataResponseDto;
import com.example.INFO.domain.favorite.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/favorites")
@RestController
@Tag(name = "기능1 좋아요 API", description = "티켓 좋아요 관리 API")
public class FavoriteController {
    private final FavoriteService favoriteService;

    @Operation(summary = "티켓 좋아요", description = "특정 티켓을 좋아요합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "티켓이 좋아요되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 필요"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/{ticketSeq}")
    public ResponseEntity<String> likeTicket(@PathVariable String ticketSeq) {
        favoriteService.likeTicket(ticketSeq);
        return ResponseEntity.ok("티켓이 좋아요되었습니다.");
    }

    @Operation(summary = "티켓 좋아요 취소", description = "좋아요한 티켓을 취소합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "티켓 좋아요가 취소되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 필요"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @DeleteMapping("/{ticketSeq}")
    public ResponseEntity<String> unlikeTicket(@PathVariable String ticketSeq) {
        favoriteService.unlikeTicket(ticketSeq);
        return ResponseEntity.ok("티켓 좋아요가 취소되었습니다.");
    }

    @Operation(summary = "좋아요한 티켓 목록 조회", description = "사용자가 좋아요한 티켓을 조회합니다. (페이징 지원)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "좋아요한 티켓 목록 반환"),
            @ApiResponse(responseCode = "401", description = "인증 필요"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/my/ticket")
    public ResponseEntity<Page<TicketDataResponseDto>> getFavoriteTickets(
            @RequestParam(defaultValue = "0") int page
    ) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdTime"));
        Page<TicketDataResponseDto> favoriteTickets = favoriteService.getFavoriteTickets(pageable);
        return ResponseEntity.ok(favoriteTickets);
    }

}
