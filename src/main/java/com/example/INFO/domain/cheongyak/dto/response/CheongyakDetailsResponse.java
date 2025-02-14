package com.example.INFO.domain.cheongyak.dto.response;

import com.example.INFO.domain.cheongyak.dto.CheongyakDetailsDto;
import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder(access = AccessLevel.PRIVATE)
public class CheongyakDetailsResponse {

    @Column(name = "id")
    Long id;

    @Column(name = "house_name")
    String houseName;

    @Column(name = "supply_location")
    String supplyLocation;

    @Column(name = "housing_type")
    String housingType;

    @Column(name = "recruitment_notice_date")
    LocalDate recruitmentNoticeDate;

    @Column(name = "supply_units")
    Integer supplyUnits;

    @Column(name = "application_start_date")
    LocalDate applicationStartDate;

    @Column(name = "application_end_date")
    LocalDate applicationEndDate;

    @Column(name = "winner_announcement_date")
    LocalDate winnerAnnouncementDate;

    @Column(name = "homepage_url")
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
