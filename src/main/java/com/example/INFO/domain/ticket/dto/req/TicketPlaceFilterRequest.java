package com.example.INFO.domain.ticket.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "공연장 필터 요청 모델")
public class TicketPlaceFilterRequest {

    @Schema(description = "필터링할 공연장 리스트")
    private List<String> places;
}