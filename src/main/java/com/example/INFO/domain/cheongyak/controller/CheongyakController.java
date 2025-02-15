package com.example.INFO.domain.cheongyak.controller;

import com.example.INFO.domain.cheongyak.dto.response.CheongyakDetailsListResponse;
import com.example.INFO.domain.cheongyak.dto.response.CheongyakDetailsResponse;
import com.example.INFO.domain.cheongyak.model.constant.CheongyakStatus;
import com.example.INFO.domain.cheongyak.service.CheongyakService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cheongyak")
@RequiredArgsConstructor
@Tag(name = "청약 API", description = "청약 관련 API")
public class CheongyakController {

    private final CheongyakService cheongyakService;

    @Operation(
            summary = "청약 목록 조회",
            description = "청약 목록을 조회합니다. 여러 조건을 필터링할 수 있습니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "청약 목록 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CheongyakDetailsListResponse.class)
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<CheongyakDetailsListResponse> list(
            @RequestParam(required = false, defaultValue = "1")
            @Parameter(description = "페이지 번호")
            Integer page,
            @RequestParam(required = false, defaultValue = "10")
            @Parameter(description = "페이지당 contents 개수")
            Integer perPage,
            @RequestParam(required = false, name = "housing_type")
            @Parameter(description = "주거 형태 필터 (예: APT, 오피스텔 등)")
            List<String> housingTypes,
            @RequestParam(required = false, name = "status")
            @Parameter(description = "청약 상태 필터 (예: ONGOING, CLOSE 등)")
            CheongyakStatus status
    ) {
        Pageable pageable = PageRequest.of(page - 1, perPage);

        Page<CheongyakDetailsResponse> cheongyakDetailsResponsePage = cheongyakService.list(pageable, housingTypes, status).map(CheongyakDetailsResponse::fromDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CheongyakDetailsListResponse.fromPage(cheongyakDetailsResponsePage));
    }
}
