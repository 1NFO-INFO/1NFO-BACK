package com.example.INFO.domain.cheongyak.dto.response;

import com.example.INFO.domain.cheongyak.dto.CheongyakDetailsDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.time.LocalDate;

@Value
public class UrbtyOfctlCheongyakDetailsResponse {

    @JsonProperty("PBLANC_NO")
    Long id;

    @JsonProperty("HOUSE_NM")
    String houseName;

    @JsonProperty("HOUSE_SECD_NM")
    String housingType;

    @JsonProperty("RCRIT_PBLANC_DE")
    String recruitmentNoticeDate;

    @JsonProperty("TOT_SUPLY_HSHLDCO")
    Integer supplyUnits;

    @JsonProperty("SUBSCRPT_RCEPT_BGNDE")
    String applicationStartDate;

    @JsonProperty("SUBSCRPT_RCEPT_ENDDE")
    String applicationEndDate;

    @JsonProperty("PRZWNER_PRESNATN_DE")
    String winnerAnnouncementDate;

    @JsonProperty("HMPG_ADRES")
    String homepageUrl;

    public CheongyakDetailsDto toDto() {
        return CheongyakDetailsDto.builder()
                .id(id)
                .houseName(houseName)
                .supplyLocation(null)
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
