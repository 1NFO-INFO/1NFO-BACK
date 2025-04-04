package com.example.INFO.domain.cheongyak.service;

import com.example.INFO.domain.cheongyak.dto.CheongyakDetailsDto;
import com.example.INFO.domain.cheongyak.repository.CheongyakDetailsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class CheongyakDetailsFetchService {

    private final AptCheongyakApiService cheongyakAptApiService;
    private final UrbtyOfctlCheongyakApiService urbtyOfctlCheongyakApiService;
    private final RemndrCheongyakApiService remndrCheongyakApiService;
    private final PblPvtRentCheongyakApiService pblPvtRentCheongyakApiService;
    private final OptCheongyakApiService optCheongyakApiService;
    private final CheongyakDetailsRepository cheongyakDetailsRepository;

    public void fetchAptData() {
        List<CheongyakDetailsDto> cheongyakDetails = cheongyakAptApiService.getAllData();

        fetchData(cheongyakDetails);
    }

    public void fetchUrbtyOfctlData() {
        List<CheongyakDetailsDto> cheongyakDetails = urbtyOfctlCheongyakApiService.getAllData();

        fetchData(cheongyakDetails);
    }

    public void fetchRemndrData() {
        List<CheongyakDetailsDto> cheongyakDetails = remndrCheongyakApiService.getAllData();

        fetchData(cheongyakDetails);
    }

    public void fetchPblPvtRentData() {
        List<CheongyakDetailsDto> cheongyakDetails = pblPvtRentCheongyakApiService.getAllData();

        fetchData(cheongyakDetails);
    }

    public void fetchOptData() {
        List<CheongyakDetailsDto> cheongyakDetails = optCheongyakApiService.getAllData();

        fetchData(cheongyakDetails);
    }

    private void fetchData(List<CheongyakDetailsDto> cheongyakDetails) {
        cheongyakDetails.stream()
                .map(CheongyakDetailsDto::toEntity)
                .forEach(cheongyakDetailsRepository::save);
    }
}
