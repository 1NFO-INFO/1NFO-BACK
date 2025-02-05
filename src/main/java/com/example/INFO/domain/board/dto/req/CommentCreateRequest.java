package com.example.INFO.domain.board.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
@Schema(description = "댓글 생성 요청 DTO")
public class CommentCreateRequest {

    @Schema(description = "게시글 ID")
    private Long boardId;

    @Schema(description = "부모 댓글 ID (대댓글인 경우만 사용)")
    private Long parentId;

    @Schema(description = "댓글 내용")
    private String content;
}