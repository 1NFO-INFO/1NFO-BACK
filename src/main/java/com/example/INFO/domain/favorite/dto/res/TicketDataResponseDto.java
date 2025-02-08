package com.example.INFO.domain.favorite.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "좋아요한 티켓 응답 DTO")
public class TicketDataResponseDto {

    @Schema(description = "티켓 식별자 (seq)", example = "12345")
    private String seq;

    @Schema(description = "티켓 제목", example = "뮤지컬 '레미제라블'")
    private String title;

    @Schema(description = "할인율", example = "20")
    private String discountRate;

    @Schema(description = "가격", example = "50000")
    private String price;

    @Schema(description = "시작 날짜", example = "2024-03-10")
    private String startDate;

    @Schema(description = "종료 날짜", example = "2024-04-10")
    private String endDate;

    @Schema(description = "공연 장소", example = "서울예술극장")
    private String place;

    @Schema(description = "지역", example = "서울")
    private String area;

    @Schema(description = "이미지 URL", example = "https://example.com/img1.jpg")
    private String img;
}
