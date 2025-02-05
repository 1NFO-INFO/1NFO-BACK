package com.example.INFO.domain.board.controller;

import com.example.INFO.domain.board.dto.res.CommentLikeResponse;
import com.example.INFO.domain.board.dto.res.LikeResponse;
import com.example.INFO.domain.board.service.LikeService;
import com.example.INFO.domain.user.service.AuthUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/likes")
@Tag(name = "좋아요 API", description = "좋아요 관련 API")
public class LikeController {

    private final LikeService likeService;
    private final AuthUserService authUserService;

    @Operation(summary = "게시글 좋아요", description = "게시글에 좋아요를 추가합니다.")
    @PostMapping("/board/{boardId}")
    public ResponseEntity<LikeResponse> likeBoard(@PathVariable Long boardId) {
        Long userId = authUserService.getAuthenticatedUserId(); // ✅ 현재 로그인된 사용자 ID 가져오기
        LikeResponse response = likeService.likeBoard(userId, boardId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "게시글 좋아요 취소", description = "게시글 좋아요를 취소합니다.")
    @DeleteMapping("/board/{boardId}")
    public ResponseEntity<LikeResponse> unlikeBoard(@PathVariable Long boardId) {
        Long userId = authUserService.getAuthenticatedUserId(); // ✅ 현재 로그인된 사용자 ID 가져오기
        LikeResponse response = likeService.unlikeBoard(userId, boardId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "댓글 좋아요", description = "댓글에 좋아요를 추가합니다.")
    @PostMapping("/comment/{commentId}")
    public ResponseEntity<CommentLikeResponse> likeComment(@PathVariable Long commentId) {
        Long userId = authUserService.getAuthenticatedUserId(); // ✅ 현재 로그인된 사용자 ID 가져오기
        CommentLikeResponse response = likeService.likeComment(userId, commentId);
        return ResponseEntity.ok(response);
    }

}
