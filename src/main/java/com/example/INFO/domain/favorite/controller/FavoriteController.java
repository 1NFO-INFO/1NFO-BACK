package com.example.INFO.domain.favorite.controller;

import com.example.INFO.domain.favorite.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/favorites")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;

    @PostMapping("/{ticketSeq}")
    public ResponseEntity<String> likeTicket(@PathVariable String ticketSeq) {
        favoriteService.likeTicket(ticketSeq);
        return ResponseEntity.ok("티켓이 좋아요되었습니다.");
    }
    @DeleteMapping("/{ticketSeq}")
    public ResponseEntity<String> unlikeTicket(@PathVariable String ticketSeq) {
        favoriteService.unlikeTicket(ticketSeq);
        return ResponseEntity.ok("티켓 좋아요가 취소되었습니다.");
    }

}
