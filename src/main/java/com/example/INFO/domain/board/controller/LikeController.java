package com.example.INFO.domain.board.controller;

import com.example.INFO.domain.board.dto.res.CommentLikeResponse;
import com.example.INFO.domain.board.dto.res.LikeResponse;
import com.example.INFO.domain.board.service.LikeService;
import com.example.INFO.domain.auth.service.AuthUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/likes")
@Tag(name = "좋아요 API", description = "좋아요 관련 API")
public class LikeController {

    private final LikeService likeService;
    private final AuthUserService authUserService;

    @Operation(summary = "게시글 좋아요", description = "게시글에 좋아요를 추가합니다.")
    @PostMapping("/board/{boardId}")
    public ResponseEntity<Map<String, Long>> likeBoard(@PathVariable Long boardId) {
        Long userId = authUserService.getAuthenticatedUserId();
        LikeResponse response = likeService.likeBoard(userId, boardId);

        Map<String, Long> result = new HashMap<>();
        result.put("boardID", response.getBoardId());
        result.put("likeID", response.getLikeId()); // Like ID 추가

        return ResponseEntity.ok(result);
    }

    @Operation(summary = "게시글 좋아요 취소", description = "게시글 좋아요를 취소합니다.")
    @DeleteMapping("/board/{boardId}")
    public ResponseEntity<Map<String, Long>> unlikeBoard(@PathVariable Long boardId) {
        Long userId = authUserService.getAuthenticatedUserId();
        LikeResponse response = likeService.unlikeBoard(userId, boardId);

        Map<String, Long> result = new HashMap<>();
        result.put("boardID", response.getBoardId());
        result.put("likeID", response.getLikeId()); // Like ID 추가

        return ResponseEntity.ok(result);
    }

    @Operation(summary = "댓글 좋아요", description = "댓글에 좋아요를 추가합니다.")
    @PostMapping("/comment/{commentId}")
    public ResponseEntity<Map<String, Long>> likeComment(@PathVariable Long commentId) {
        Long userId = authUserService.getAuthenticatedUserId();
        CommentLikeResponse response = likeService.likeComment(userId, commentId);

        Map<String, Long> result = new HashMap<>();
        result.put("commentID", response.getCommentId());
        result.put("likeID", response.getLikeId()); // Like ID 추가

        return ResponseEntity.ok(result);
    }

    @Operation(summary = "댓글 좋아요 취소", description = "댓글 좋아요를 취소합니다.")
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Map<String, Long>> unlikeComment(@PathVariable Long commentId) {
        Long userId = authUserService.getAuthenticatedUserId();
        CommentLikeResponse response = likeService.unlikeComment(userId, commentId);

        Map<String, Long> result = new HashMap<>();
        result.put("commentID", response.getCommentId());
        result.put("likeID", response.getLikeId()); // Like ID 추가

        return ResponseEntity.ok(result);
    }


}
