package com.example.INFO.domain.favorite.controller;

import com.example.INFO.domain.favorite.dto.res.FavoriteResponseDto;
import com.example.INFO.domain.favorite.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/favorites")
@RestController
@Tag(name = "좋아요 API", description = "좋아요 관리 API")
public class FavoriteController {
    private final FavoriteService favoriteService;

    @Operation(summary = "좋아요", description = "특정 항목을 좋아요합니다.")
    @PostMapping("/{entityType}/{entityId}")
    public ResponseEntity<FavoriteResponseDto> likeEntity(
            @PathVariable String entityType,
            @PathVariable String entityId) {
        return ResponseEntity.ok(favoriteService.likeEntity(entityId, entityType));
    }

    @Operation(summary = "좋아요 취소", description = "좋아요한 항목을 취소합니다.")
    @DeleteMapping("/{entityType}/{entityId}")
    public ResponseEntity<String> unlikeEntity(
            @PathVariable String entityType,
            @PathVariable String entityId) {
        favoriteService.unlikeEntity(entityId, entityType);
        return ResponseEntity.ok("좋아요가 취소되었습니다.");
    }

    @Operation(summary = "좋아요한 항목 조회", description = "사용자가 좋아요한 항목을 최신순으로 조회합니다.")
    @GetMapping("/my")
    public ResponseEntity<List<FavoriteResponseDto>> getFavoriteEntities() {
        return ResponseEntity.ok(favoriteService.getFavoriteEntities());
    }
}
