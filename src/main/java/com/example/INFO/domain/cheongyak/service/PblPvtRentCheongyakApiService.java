package com.example.INFO.domain.cheongyak.service;

import com.example.INFO.domain.cheongyak.dto.CheongyakDetailsDto;
import com.example.INFO.domain.cheongyak.dto.response.CheongyakApiResponse;
import com.example.INFO.domain.cheongyak.dto.response.PblPvtRentCheongyakDetailsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PblPvtRentCheongyakApiService extends AbstractCheongyakApiService<PblPvtRentCheongyakDetailsResponse> {

    @Value("${api.cheongyak.url.pbl-pvt-rent}")
    private String pblPvtRentUrl;

    public PblPvtRentCheongyakApiService(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    protected String getBaseUrl() {
        return pblPvtRentUrl;
    }

    @Override
    protected CheongyakDetailsDto convertToDto(PblPvtRentCheongyakDetailsResponse response) {
        return response.toDto();
    }

    @Override
    protected ParameterizedTypeReference<CheongyakApiResponse<PblPvtRentCheongyakDetailsResponse>> getResponseType() {
        return new ParameterizedTypeReference<>() {};
    }
}
