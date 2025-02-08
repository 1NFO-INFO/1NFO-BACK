package com.example.INFO.domain.user.service;

import com.example.INFO.domain.user.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Log4j2
public class RefreshTokenCleanupScheduler {

    private final RefreshTokenRepository refreshTokenRepository;

    @Scheduled(cron = "0 0 3 * * *")    // 매일 새벽 3시 0분 0초
    public void deleteExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        log.info("리프레쉬 토큰 청소 작업 수행");
        refreshTokenRepository.deleteByExpirationBefore(now);
    }
}
