package com.example.INFO.domain.board.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@Schema(description = "댓글 상세 응답 DTO")
public class CommentResponse {

    @Schema(description = "댓글 ID")
    private Long id;

    @Schema(description = "게시글 ID")
    private Long boardId;

    @Schema(description = "작성자 ID")
    private Long userId;

    @Schema(description = "댓글 내용")
    private String content;

    @Schema(description = "댓글 생성 시간")
    private LocalDateTime createdTime;

    @Schema(description = "댓글 수정 시간")
    private LocalDateTime updatedTime;

    @Schema(description = "대댓글 리스트")
    private List<CommentResponse> replies;

    @Schema(description = "댓글 좋아요 개수")
    private int likeCount;
}
