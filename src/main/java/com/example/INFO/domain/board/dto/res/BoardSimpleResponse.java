package com.example.INFO.domain.board.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
@Schema(description = "게시글 간략한 응답 DTO")
public class BoardSimpleResponse {
    @Schema(description = "게시글 제목")
    private String title;      // 게시글 제목
    @Schema(description = "게시글 내용")
    private String content;    // 게시글 내용
    @Schema(description = "게시글 좋아요 개수")
    private int likeCount;     // 게시글 좋아요 개수
    @Schema(description = "게시글 댓글 개수")
    private int commentCount;  // 게시글 댓글 개수

}
