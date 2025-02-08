package com.example.INFO.domain.ticket.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketData {
    @Id
    private String seq;

    private String title;
    private String discountRate;
    private String price;
    private String startDate;
    private String endDate;
    private String place;
    private String area;
    private String img;
    private String imgDesc;
}
