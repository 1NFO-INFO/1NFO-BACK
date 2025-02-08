package com.example.INFO.domain.favorite.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "페이징 응답 DTO")
public class PagedResponseDto<T> {

    @Schema(description = "데이터 목록", example = "[{...}, {...}]")
    private List<T> content; // 데이터 리스트

    @Schema(description = "현재 페이지 번호 (0부터 시작)", example = "0")
    private int page; // 현재 페이지

    @Schema(description = "한 페이지당 개수", example = "10")
    private int size; // 한 페이지당 개수

    @Schema(description = "전체 데이터 개수", example = "50")
    private long totalElements; // 전체 데이터 개수

    @Schema(description = "전체 페이지 수", example = "5")
    private int totalPages; // 전체 페이지 수

    @Schema(description = "마지막 페이지 여부", example = "false")
    private boolean last; // 마지막 페이지 여부
}
