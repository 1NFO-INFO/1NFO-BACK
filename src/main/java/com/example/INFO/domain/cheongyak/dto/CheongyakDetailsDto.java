package com.example.INFO.domain.cheongyak.dto;

import com.example.INFO.domain.cheongyak.model.entity.CheongyakDetailsEntity;
import lombok.*;

import java.time.LocalDate;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class CheongyakDetailsDto {

    Long id;
    String houseName;
    String supplyLocation;
    String housingType;
    LocalDate recruitmentNoticeDate;
    Integer supplyUnits;
    LocalDate applicationStartDate;
    LocalDate applicationEndDate;
    LocalDate winnerAnnouncementDate;
    String homepageUrl;

    public CheongyakDetailsEntity toEntity() {
        return CheongyakDetailsEntity.builder(id)
                .houseName(houseName)
                .supplyLocation(supplyLocation)
                .housingType(housingType)
                .recruitmentNoticeDate(recruitmentNoticeDate)
                .supplyUnits(supplyUnits)
                .applicationStartDate(applicationStartDate)
                .applicationEndDate(applicationEndDate)
                .winnerAnnouncementDate(winnerAnnouncementDate)
                .homepageUrl(homepageUrl)
                .build();
    }

    public static CheongyakDetailsDto fromEntity(CheongyakDetailsEntity cheongyakDetailsEntity) {
        return CheongyakDetailsDto.builder(cheongyakDetailsEntity.getId())
                .houseName(cheongyakDetailsEntity.getHouseName())
                .supplyLocation(cheongyakDetailsEntity.getSupplyLocation())
                .housingType(cheongyakDetailsEntity.getHousingType())
                .recruitmentNoticeDate(cheongyakDetailsEntity.getRecruitmentNoticeDate())
                .supplyUnits(cheongyakDetailsEntity.getSupplyUnits())
                .applicationStartDate(cheongyakDetailsEntity.getApplicationStartDate())
                .applicationEndDate(cheongyakDetailsEntity.getApplicationEndDate())
                .winnerAnnouncementDate(cheongyakDetailsEntity.getWinnerAnnouncementDate())
                .homepageUrl(cheongyakDetailsEntity.getHomepageUrl())
                .build();
    }

    public static CheongyakDetailsDtoBuilder builder(Long id) {
        return new CheongyakDetailsDtoBuilder().id(id);
    }
}
