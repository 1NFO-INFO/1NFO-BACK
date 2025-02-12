package com.example.INFO.domain.cheongyak.service;

import com.example.INFO.domain.cheongyak.dto.CheongyakDetailsDto;
import com.example.INFO.domain.cheongyak.dto.response.CheongyakApiResponse;
import com.example.INFO.domain.cheongyak.dto.response.OptCheongyakDetailsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class OptCheongyakApiService extends AbstractCheongyakApiService<OptCheongyakDetailsResponse> {

    @Value("${api.cheongyak.url.opt}")
    private String optUrl;

    public OptCheongyakApiService(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    protected String getBaseUrl() {
        return optUrl;
    }

    @Override
    protected CheongyakDetailsDto convertToDto(OptCheongyakDetailsResponse response) {
        return response.toDto();
    }

    @Override
    protected ParameterizedTypeReference<CheongyakApiResponse<OptCheongyakDetailsResponse>> getResponseType() {
        return new ParameterizedTypeReference<>() {};
    }
}
