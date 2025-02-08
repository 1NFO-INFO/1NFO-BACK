package com.example.INFO.domain.board.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
@Schema(description = "게시글 상세 응답 DTO")
public class BoardResponse {
    @Schema(description = "게시글 ID")
    private Long boardId; // 게시글 ID
    @Schema(description = "게시글 제목")
    private String title; // 제목
    @Schema(description = "게시글 내용")
    private String content; // 내용
    @Schema(description = "게시글 카테고리")
    private String categoryName; // 카테고리 이름
    @Schema(description = "게시글 이미지 URL")
    private String postImage; // 이미지 URL
    @Schema(description = "게시글 생성 시간")
    private LocalDateTime createdTime; // 생성 시간
    @Schema(description = "게시글 수정 시간")
    private LocalDateTime updatedTime; // 수정 시간
    @Schema(description = "게시글 좋아요 개수")
    private int likeCount;

}
