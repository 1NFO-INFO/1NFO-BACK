package com.example.INFO.domain.cheongyak.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "cheongyak_detail")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class CheongyakDetailsEntity {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "house_name")
    private String houseName;

    @Column(name = "supply_location")
    private String supplyLocation;

    @Column(name = "housing_type")
    private String housingType;

    @Column(name = "recruitment_notice_date")
    private LocalDate recruitmentNoticeDate;

    @Column(name = "supply_units")
    private Integer supplyUnits;

    @Column(name = "application_start_date")
    private LocalDate applicationStartDate;

    @Column(name = "application_end_date")
    private LocalDate applicationEndDate;

    @Column(name = "winner_announcement_date")
    private LocalDate winnerAnnouncementDate;

    @Column(name = "homepage_url")
    private String homepageUrl;

    public static CheongyakDetailsEntityBuilder builder(Long id) {
        return new CheongyakDetailsEntityBuilder().id(id);
    }
}
