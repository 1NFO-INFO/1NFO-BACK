package com.example.INFO.domain.cheongyak.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.List;

@Value
public class CheongyakApiResponse<T> {

    @JsonProperty("currentCount")
    int currentCount;

    @JsonProperty("data")
    List<T> data;

    @JsonProperty("matchCount")
    int matchCount;

    @JsonProperty("page")
    int page;

    @JsonProperty("perPage")
    int perPage;

    @JsonProperty("totalCount")
    int totalCount;
}
