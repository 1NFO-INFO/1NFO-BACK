package com.example.INFO.domain.cheongyak.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class CheongyakFetchScheduler {

    private final CheongyakDetailsFetchService cheongyakDetailsFetchService;

    @Scheduled(cron = "0 0 3 * * *")    // 매일 새벽 3시 0분 0초
    public void fetchCheongyak() {
        fetchAptCheongyak();
        fetchUrbtyOfctlCheongyak();
        fetchRemndrCheongyak();
        fetchPblPvtRentCheongyak();
        fetchOptCheongyak();
    }

    private void fetchAptCheongyak() {
        try {
            log.info("청약 상세정보 fetch 작업 시작 - APT");
            cheongyakDetailsFetchService.fetchAptData();
            log.info("청약 상세정보 fetch 작업 성공 - APT");
        } catch (Exception e) {
            log.error("청약 상세정보 fetch 작업 실패 - APT", e);
        }
    }

    private void fetchUrbtyOfctlCheongyak() {
        try {
            log.info("청약 상세정보 fetch 작업 시작 - 오피스텔/도시형/민간임대/생활숙박시설");
            cheongyakDetailsFetchService.fetchUrbtyOfctlData();
            log.info("청약 상세정보 fetch 작업 성공 - 오피스텔/도시형/민간임대/생활숙박시설");
        } catch (Exception e) {
            log.error("청약 상세정보 fetch 작업 실패 - 오피스텔/도시형/민간임대/생활숙박시설", e);
        }
    }

    private void fetchRemndrCheongyak() {
        try {
            log.info("청약 상세정보 fetch 작업 시작 - APT 무순위/잔여세대");
            cheongyakDetailsFetchService.fetchRemndrData();
            log.info("청약 상세정보 fetch 작업 성공 - APT 무순위/잔여세대");
        } catch (Exception e) {
            log.error("청약 상세정보 fetch 작업 실패 - APT 무순위/잔여세대", e);
        }
    }

    private void fetchPblPvtRentCheongyak() {
        try {
            log.info("청약 상세정보 fetch 작업 시작 - 공공지원 민간임대");
            cheongyakDetailsFetchService.fetchPblPvtRentData();
            log.info("청약 상세정보 fetch 작업 성공 - 공공지원 민간임대");
        } catch (Exception e) {
            log.error("청약 상세정보 fetch 작업 실패 - 공공지원 민간임대", e);
        }
    }

    private void fetchOptCheongyak() {
        try {
            log.info("청약 상세정보 fetch 작업 시작 - 임의공급");
            cheongyakDetailsFetchService.fetchOptData();
            log.info("청약 상세정보 fetch 작업 성공 - 임의공급");
        } catch (Exception e) {
            log.error("청약 상세정보 fetch 작업 실패 - 임의공급", e);
        }
    }
}
