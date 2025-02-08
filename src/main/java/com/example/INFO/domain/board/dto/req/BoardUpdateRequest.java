package com.example.INFO.domain.board.dto.req;

import com.example.INFO.domain.board.domain.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
@Schema(description = "게시글 수정 요청 DTO")
public class BoardUpdateRequest {

    @NotNull
    @Size(min = 1, max = 50)
    @Schema(description = "게시글 제목", example = "수정된 제목")
    private String title;

    @NotNull
    @Schema(description = "게시글 내용", example = "수정된 게시글 내용")
    private String content;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Schema(description = "게시글 카테고리")
    private Category categoryName;

    @Schema(description = "게시글 이미지 URL")
    private String postImage;
}