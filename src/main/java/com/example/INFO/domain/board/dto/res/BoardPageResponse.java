package com.example.INFO.domain.board.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@Schema(description = "게시글 페이징 응답 DTO")
public class BoardPageResponse {
    @Schema(description = "게시글 목록")
    private List<BoardSimpleResponse> content; // 기존 BoardResponse → BoardSimpleResponse 변경

    @Schema(description = "현재 페이지 번호")
    private int currentPage;  // 현재 페이지

    @Schema(description = "전체 페이지 수")
    private int totalPages;   // 전체 페이지 수
    @Schema(description = "전체 게시글 수")
    private long totalElements; // 전체 게시글 수
    @Schema(description = "마지막 페이지 여부")
    private boolean last;     // 마지막 페이지 여부

}
