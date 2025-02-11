package com.example.INFO.domain.cheongyak.dto.response;

import com.example.INFO.domain.cheongyak.dto.CheongyakDetailsDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.time.LocalDate;

@Value
public class AptCheongyakDetailsResponse {

    @JsonProperty("PBLANC_NO")
    String id;

    @JsonProperty("HOUSE_NM")
    String houseName;

    @JsonProperty("SUBSCRPT_AREA_CODE_NM")
    String supplyLocation;

    @JsonProperty("HOUSE_SECD_NM")
    String housingType;

    @JsonProperty("RCRIT_PBLANC_DE")
    String recruitmentNoticeDate;

    @JsonProperty("TOT_SUPLY_HSHLDCO")
    Integer supplyUnits;

    @JsonProperty("RCEPT_BGNDE")
    String applicationStartDate;

    @JsonProperty("RCEPT_ENDDE")
    String applicationEndDate;

    @JsonProperty("PRZWNER_PRESNATN_DE")
    String winnerAnnouncementDate;

    @JsonProperty("HMPG_ADRES")
    String homepageUrl;

    public CheongyakDetailsDto toDto() {
        return CheongyakDetailsDto.builder()
                .id(id)
                .houseName(houseName)
                .supplyLocation(supplyLocation)
                .housingType(housingType)
                .recruitmentNoticeDate(LocalDate.parse(recruitmentNoticeDate))
                .supplyUnits(supplyUnits)
                .applicationStartDate(LocalDate.parse(applicationStartDate))
                .applicationEndDate(LocalDate.parse(applicationEndDate))
                .winnerAnnouncementDate(LocalDate.parse(winnerAnnouncementDate))
                .homepageUrl(homepageUrl)
                .build();
    }
}
