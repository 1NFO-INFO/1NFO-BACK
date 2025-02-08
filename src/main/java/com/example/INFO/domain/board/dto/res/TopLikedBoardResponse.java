package com.example.INFO.domain.board.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "좋아요 상위 게시글 응답 DTO")
public class TopLikedBoardResponse {
    @Schema(description = "게시글 제목")
    private String title;
    @Schema(description = "게시글 카테고리")
    private String category;
    @Schema(description = "게시글 생성 시간")
    private LocalDateTime createdTime;
    @Schema(description = "게시글 수정 시간")
    private LocalDateTime updatedTime;

}
