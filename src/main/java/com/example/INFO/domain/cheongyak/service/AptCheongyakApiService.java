package com.example.INFO.domain.cheongyak.service;

import com.example.INFO.domain.cheongyak.dto.CheongyakDetailsDto;
import com.example.INFO.domain.cheongyak.dto.response.AptCheongyakDetailsResponse;
import com.example.INFO.domain.cheongyak.dto.response.CheongyakApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AptCheongyakApiService extends AbstractCheongyakApiService<AptCheongyakDetailsResponse> {

    @Value("${api.cheongyak.url.apt}")
    private String aptUrl;

    public AptCheongyakApiService(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    protected String getBaseUrl() {
        return aptUrl;
    }

    @Override
    protected CheongyakDetailsDto convertToDto(AptCheongyakDetailsResponse response) {
        return response.toDto();
    }

    @Override
    protected ParameterizedTypeReference<CheongyakApiResponse<AptCheongyakDetailsResponse>> getResponseType() {
        return new ParameterizedTypeReference<>() {};
    }
}
