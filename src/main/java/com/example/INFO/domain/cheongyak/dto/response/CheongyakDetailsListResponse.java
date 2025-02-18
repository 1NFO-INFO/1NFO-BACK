package com.example.INFO.domain.cheongyak.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.data.domain.Page;

import java.util.List;

@Value
@RequiredArgsConstructor(staticName = "from", access = AccessLevel.PRIVATE)
@Schema(description = "청약 상세 목록 응답")
public class CheongyakDetailsListResponse {

    @JsonProperty("contents")
    @Schema(description = "청약 상세 목록", implementation = CheongyakDetailsResponse.class)
    List<CheongyakDetailsResponse> cheongyakDetailsResponses;

    @JsonProperty("page")
    @Schema(description = "페이지 정보", implementation = PageInfo.class)
    PageInfo pageInfo;

    @Value
    @Builder(access = AccessLevel.PRIVATE)
    @Schema(description = "페이지 정보")
    private static class PageInfo {
        @JsonProperty("total_pages")
        @Schema(description = "전체 페이지 수")
        int totalPages;

        @JsonProperty("total_contents")
        @Schema(description = "전체 contents 수")
        long totalElements;

        @JsonProperty("current_page")
        @Schema(description = "현재 페이지 번호")
        int currentPage;

        @JsonProperty("per_page")
        @Schema(description = "페이지당 아이템 수")
        int perPage;
    }

    public static CheongyakDetailsListResponse fromPage(Page<CheongyakDetailsResponse> cheongyakDetailsResponses) {
        PageInfo pageInfo = PageInfo.builder()
                .totalPages(cheongyakDetailsResponses.getTotalPages())
                .totalElements(cheongyakDetailsResponses.getTotalElements())
                .currentPage(cheongyakDetailsResponses.getPageable().getPageNumber() + 1)
                .perPage(cheongyakDetailsResponses.getPageable().getPageSize())
                .build();

        return new CheongyakDetailsListResponse(cheongyakDetailsResponses.toList(), pageInfo);
    }
}
