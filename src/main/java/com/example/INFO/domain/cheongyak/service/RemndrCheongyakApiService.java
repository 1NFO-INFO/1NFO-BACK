package com.example.INFO.domain.cheongyak.service;

import com.example.INFO.domain.cheongyak.dto.CheongyakDetailsDto;
import com.example.INFO.domain.cheongyak.dto.response.CheongyakApiResponse;
import com.example.INFO.domain.cheongyak.dto.response.RemndrCheongyakDetailsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RemndrCheongyakApiService extends AbstractCheongyakApiService<RemndrCheongyakDetailsResponse> {

    @Value("${api.cheongyak.url.remndr}")
    private String remndrUrl;

    public RemndrCheongyakApiService(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    protected String getBaseUrl() {
        return remndrUrl;
    }

    @Override
    protected CheongyakDetailsDto convertToDto(RemndrCheongyakDetailsResponse response) {
        return response.toDto();
    }

    @Override
    protected ParameterizedTypeReference<CheongyakApiResponse<RemndrCheongyakDetailsResponse>> getResponseType() {
        return new ParameterizedTypeReference<>() {};
    }
}
