package com.example.INFO.domain.cheongyak.service;

import com.example.INFO.domain.cheongyak.dto.CheongyakDetailsDto;
import com.example.INFO.domain.cheongyak.dto.response.CheongyakApiResponse;
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

@Log4j2
@Service
@RequiredArgsConstructor
public abstract class AbstractCheongyakApiService<T> {

    private final RestTemplate restTemplate;

    @Value("${api.cheongyak.key}")
    private String apiKey;

    @Value("${api.cheongyak.per-page}")
    private int perPage;

    protected abstract String getBaseUrl();
    protected abstract CheongyakDetailsDto convertToDto(T response);

    public List<CheongyakDetailsDto> getAllData() {
        final int totalCount = getApiResponse(1, 1).getTotalCount();

        List<CheongyakDetailsDto> cheongyakData = new ArrayList<>();
        for (int page = 1; page <= (totalCount / perPage) + 1; page++) {
            cheongyakData.addAll(getData(page, perPage));
        }

        return cheongyakData;
    }

    private List<CheongyakDetailsDto> getData(int page, int perPage) {
        List<CheongyakDetailsDto> cheongyakDetails = new ArrayList<>();

        CheongyakApiResponse<T> apiResponse = getApiResponse(page, perPage);

        apiResponse.getData().stream()
                .map(this::convertToDto)
                .forEach(cheongyakDetails::add);

        return cheongyakDetails;
    }

    private CheongyakApiResponse<T> getApiResponse(int page, int perPage) {
        String url = buildUrl(page, perPage);
        log.info("API 호출: {}", url);

        try {
            return restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    getResponseType()
            ).getBody();
        } catch (RestClientException e) {
            log.error("API 호출 중 오류 발생: {}", url, e);
            throw new RuntimeException("API 호출 중 오류 발생", e);
        }
    }

    private String buildUrl(int page, int perPage) {
        return UriComponentsBuilder.fromUriString(getBaseUrl())
                .queryParam("serviceKey", apiKey)
                .queryParam("page", page)
                .queryParam("perPage", perPage)
                .build(false)
                .toUriString();
    }

    protected abstract ParameterizedTypeReference<CheongyakApiResponse<T>> getResponseType();
}
