package com.example.INFO.domain.cheongyak.service;

import com.example.INFO.domain.cheongyak.dto.CheongyakDetailsDto;
import com.example.INFO.domain.cheongyak.dto.response.CheongyakApiResponse;
import com.example.INFO.domain.cheongyak.dto.response.OptCheongyakDetailsResponse;
import com.example.INFO.domain.cheongyak.dto.response.PblPvtRentCheongyakDetailsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class OptCheongyakApiService {

    private final RestTemplate restTemplate;

    @Value("${api.cheongyak.url.opt}")
    private String urbtyOfCtlUrl;
    @Value("${api.cheongyak.key}")
    private String apiKey;
    @Value("${api.cheongyak.per-page}")
    private int perPage;

    public List<CheongyakDetailsDto> getAllData() {
        final int totalCount = getAptResponse(1, 1).getTotalCount();

        List<CheongyakDetailsDto> cheongyakData = new ArrayList<>();
        for (int page = 1; page <= (totalCount / perPage) + 1; page++) {
            cheongyakData.addAll(getAptData(page, perPage));
        }

        return cheongyakData;
    }

    private List<CheongyakDetailsDto> getAptData(int page, int perPage) {
        List<CheongyakDetailsDto> cheongyakDetails = new ArrayList<>();

        CheongyakApiResponse<OptCheongyakDetailsResponse> apiResponse = getAptResponse(page, perPage);

        apiResponse.getData().stream()
                .map(OptCheongyakDetailsResponse::toDto)
                .forEach(cheongyakDetails::add);

        return cheongyakDetails;
    }

    private CheongyakApiResponse<OptCheongyakDetailsResponse> getAptResponse(int page, int perPage) {
        String url = buildAptUrl(page, perPage);
        log.info("API 호출: {}", url);

        try {
            return restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<CheongyakApiResponse<OptCheongyakDetailsResponse>>() {}
            ).getBody();
        } catch (RestClientException e) {
            log.error("API 호출 중 오류 발생: {}", url, e);
            throw new RuntimeException("API 호출 중 오류 발생", e);
        }
    }

    private String buildAptUrl(int page, int perPage) {
        return UriComponentsBuilder.fromUriString(urbtyOfCtlUrl)
                .queryParam("serviceKey", apiKey)
                .queryParam("page", page)
                .queryParam("perPage", perPage)
                .build(false)
                .toUriString();
    }
}
