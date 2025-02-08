package com.example.INFO.domain.ticket.domain;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "api")
public class TicketProperties {
    @NotEmpty
    private String baseUrl;
    @NotEmpty
    private String apikey;
}
