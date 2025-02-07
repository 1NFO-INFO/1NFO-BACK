package com.example.INFO.domain.favorite.controller;

import com.example.INFO.domain.favorite.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
