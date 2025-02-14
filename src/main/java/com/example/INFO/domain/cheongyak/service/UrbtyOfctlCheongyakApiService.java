package com.example.INFO.domain.cheongyak.service;

import com.example.INFO.domain.cheongyak.dto.CheongyakDetailsDto;
import com.example.INFO.domain.cheongyak.dto.response.CheongyakApiResponse;
import com.example.INFO.domain.cheongyak.dto.response.UrbtyOfctlCheongyakDetailsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UrbtyOfctlCheongyakApiService extends AbstractCheongyakApiService<UrbtyOfctlCheongyakDetailsResponse> {

    @Value("${api.cheongyak.url.urbty-of-ctl}")
    private String urbtyOfCtlUrl;

    public UrbtyOfctlCheongyakApiService(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    protected String getBaseUrl() {
        return urbtyOfCtlUrl;
    }

    @Override
    protected CheongyakDetailsDto convertToDto(UrbtyOfctlCheongyakDetailsResponse response) {
        return response.toDto();
    }

    @Override
    protected ParameterizedTypeReference<CheongyakApiResponse<UrbtyOfctlCheongyakDetailsResponse>> getResponseType() {
        return new ParameterizedTypeReference<>() {};
    }
}
