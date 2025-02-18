package com.example.INFO.domain.favorite.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "좋아요한 항목 응답 DTO")
public class FavoriteResponseDto {

    @Schema(description = "좋아요 ID", example = "1")
    private Long favoriteId;

    @Schema(description = "좋아요한 항목의 식별자 (seq)", example = "12345")
    private String entityId;

    @Schema(description = "좋아요한 항목의 유형 (TICKET, SUBSCRIPTION, SUPPORT_POLICY)", example = "TICKET")
    private String entityType;

    @Schema(description = "좋아요한 항목의 제목", example = "뮤지컬 '레미제라블'")
    private String title; // ✅ 추가

    @Schema(description = "좋아요한 날짜", example = "2024-03-15T10:00:00")
    private LocalDateTime createdTime;
}
