package com.example.INFO.domain.favorite.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "티켓 좋아요 요청 DTO")
public class FavoriteRequestDto {

    @Schema(description = "티켓 식별자 (seq)", example = "12345")
    private String ticketSeq;
}