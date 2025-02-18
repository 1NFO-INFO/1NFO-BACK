package com.example.INFO.domain.cheongyak.dto.response;

import com.example.INFO.domain.cheongyak.dto.CheongyakDetailsDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder(access = AccessLevel.PRIVATE)
public class CheongyakDetailsResponse {

    @JsonProperty("id")
    @Schema(description = "청약 공고 ID")
    Long id;

    @JsonProperty("house_name")
    @Schema(description = "아파트 이름")
    String houseName;

    @JsonProperty("supply_location")
    @Schema(description = "공급 위치")
    String supplyLocation;

    @JsonProperty("housing_type")
    @Schema(description = "주거 형태")
    String housingType;

    @JsonProperty("recruitment_notice_date")
    @Schema(description = "공고일")
    LocalDate recruitmentNoticeDate;

    @JsonProperty("supply_units")
    @Schema(description = "공급 세대 수")
    Integer supplyUnits;

    @JsonProperty("application_start_date")
    @Schema(description = "청약 접수 시작일")
    LocalDate applicationStartDate;

    @JsonProperty("application_end_date")
    @Schema(description = "청약 접수 마감일")
    LocalDate applicationEndDate;

    @JsonProperty("winner_announcement_date")
    @Schema(description = "당첨자 발표일")
    LocalDate winnerAnnouncementDate;

    @JsonProperty("homepage_url")
    @Schema(description = "홈페이지 URL")
    String homepageUrl;

    public static CheongyakDetailsResponse fromDto(CheongyakDetailsDto cheongyakDetailsDto) {
        return CheongyakDetailsResponse.builder()
                .id(cheongyakDetailsDto.getId())
                .houseName(cheongyakDetailsDto.getHouseName())
                .supplyLocation(cheongyakDetailsDto.getSupplyLocation())
                .housingType(cheongyakDetailsDto.getHousingType())
                .recruitmentNoticeDate(cheongyakDetailsDto.getRecruitmentNoticeDate())
                .supplyUnits(cheongyakDetailsDto.getSupplyUnits())
                .applicationStartDate(cheongyakDetailsDto.getApplicationStartDate())
                .applicationEndDate(cheongyakDetailsDto.getApplicationEndDate())
                .winnerAnnouncementDate(cheongyakDetailsDto.getWinnerAnnouncementDate())
                .homepageUrl(cheongyakDetailsDto.getHomepageUrl())
                .build();
    }
}
