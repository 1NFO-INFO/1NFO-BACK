package com.example.INFO.domain.ticket.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "티켓 응답 데이터 모델")
public class TicketResponse {

    @Schema(description = "공연 제목")
    private String title;

    @Schema(description = "할인율")
    private String discountRate;

    @Schema(description = "가격")
    private String price;

    @Schema(description = "공연 시작일")
    private String startDate;

    @Schema(description = "공연 종료일")
    private String endDate;

    @Schema(description = "공연장")
    private String place;

    @Schema(description = "공연 이미지 URL")
    private String img;
}
