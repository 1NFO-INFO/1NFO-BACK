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
            log.info("아파트 청약 상세정보 fetch 작업 시작");
            cheongyakDetailsFetchService.fetchAptData();
            log.info("아파트 청약 상세정보 fetch 작업 성공 - APT");
        } catch (Exception e) {
            log.error("아파트 청약 상세정보 fetch 작업 실패 - APT", e);
        }
    }

    private void fetchUrbtyOfctlCheongyak() {
        try {
            log.info("오피스텔/도시형/민간임대/생활숙박시설 청약 상세정보 fetch 작업 시작");
            cheongyakDetailsFetchService.fetchUrbtyOfctlData();
            log.info("오피스텔/도시형/민간임대/생활숙박시설 청약 상세정보 fetch 작업 성공 - APT");
        } catch (Exception e) {
            log.error("오피스텔/도시형/민간임대/생활숙박시설 청약 상세정보 fetch 작업 실패 - APT", e);
        }
    }

    private void fetchRemndrCheongyak() {
        try {
            log.info("APT 무순위/잔여세대 청약 상세정보 fetch 작업 시작");
            cheongyakDetailsFetchService.fetchRemndrData();
            log.info("APT 무순위/잔여세대 청약 상세정보 fetch 작업 성공");
        } catch (Exception e) {
            log.error("APT 무순위/잔여세대 청약 상세정보 fetch 작업 실패", e);
        }
    }

    private void fetchPblPvtRentCheongyak() {
        try {
            log.info("공공지원 민간임대 청약 상세정보 fetch 작업 시작");
            cheongyakDetailsFetchService.fetchPblPvtRentData();
            log.info("공공지원 민간임대 청약 상세정보 fetch 작업 성공");
        } catch (Exception e) {
            log.error("공공지원 민간임대 청약 상세정보 fetch 작업 실패", e);
        }
    }

    private void fetchOptCheongyak() {
        try {
            log.info("임의공급 청약 상세정보 fetch 작업 시작");
            cheongyakDetailsFetchService.fetchOptData();
            log.info("임의공급 청약 상세정보 fetch 작업 성공");
        } catch (Exception e) {
            log.error("임의공급 청약 상세정보 fetch 작업 실패", e);
        }
    }
}
