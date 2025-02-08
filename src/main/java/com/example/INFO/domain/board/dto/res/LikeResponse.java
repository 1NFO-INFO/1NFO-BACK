package com.example.INFO.domain.board.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
@Schema(description = "게시글 좋아요 응답 DTO")
public class LikeResponse {
    @Schema(description = "게시글 ID")
    private Long boardId;
    @Schema(description = "좋아요 ID")
    private Long likeId;
    @Schema(description = "게시글 좋아요 개수")
    private int likeCount; // 좋아요 개수
}
