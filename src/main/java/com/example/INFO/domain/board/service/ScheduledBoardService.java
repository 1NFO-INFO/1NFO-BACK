package com.example.INFO.domain.board.service;

import com.example.INFO.domain.board.domain.Board;
import com.example.INFO.domain.board.domain.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduledBoardService {

    private final BoardRepository boardRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 * * *") // 매일 00:00 실행
    public void updateTop3LikedPostsFromYesterday() {
        ZoneId zoneId = ZoneId.of("Asia/Seoul");
        LocalDateTime start = LocalDate.now(zoneId).minusDays(1).atStartOfDay();
        LocalDateTime end = start.plusDays(1).minusNanos(1);

        log.info("🔥 어제 인기 게시글 조회: {} ~ {}", start, end);

        List<Board> top3Posts = boardRepository.findTop3ByLikes(start, end, PageRequest.of(0, 3));

        log.info("📌 조회된 게시글 개수: {}", top3Posts.size());

        for (Board board : top3Posts) {
            log.info("✅ 인기 게시글 - 제목: {}, 좋아요 개수: {}", board.getTitle(), board.getLikeCount());
        }
    }
}
