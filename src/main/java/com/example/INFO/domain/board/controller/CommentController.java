package com.example.INFO.domain.board.controller;

import com.example.INFO.domain.board.dto.req.CommentCreateRequest;
import com.example.INFO.domain.board.dto.res.CommentResponse;
import com.example.INFO.domain.board.service.CommentService;
import com.example.INFO.domain.user.service.AuthUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Tag(name = "댓글 API", description = "댓글 관련 API")
public class CommentController {
    private final CommentService commentService;
    private final AuthUserService authUserService;

    // 댓글 생성
    @PostMapping
    @Operation(summary = "댓글 작성", description = "게시글에 댓글을 작성합니다.")
    public ResponseEntity<String> createComment(@RequestBody CommentCreateRequest request) {
        Long userId = authUserService.getAuthenticatedUserId(); // ✅ 로그인된 사용자 정보 가져오기
        CommentResponse response = commentService.createComment(userId, request);
        return ResponseEntity.ok("boardId: "+response.getBoardId()+"CommentID: "+response.getId());
    }
    // 댓글 및 대댓글 조회
    @Operation(summary = "게시글의 댓글 및 대댓글 조회", description = "게시글 ID를 기준으로 모든 댓글과 대댓글을 조회합니다.")
    @GetMapping("/search/board/{boardId}")
    public ResponseEntity<List<CommentResponse>> getCommentsWithReplies(@PathVariable Long boardId) {
        List<CommentResponse> responses = commentService.getCommentsWithReplies(boardId);
        return ResponseEntity.ok(responses);
    }
    // 댓글 수정
    @Operation(summary = "댓글 수정", description = "댓글 내용을 수정합니다.")
    @PutMapping("/{commentId}")
    public ResponseEntity<String> updateComment(@PathVariable Long commentId, @RequestParam String content) {
        Long userId = authUserService.getAuthenticatedUserId(); // ✅ 로그인된 사용자 정보 가져오기
        CommentResponse response = commentService.updateComment(userId, commentId, content);
        return ResponseEntity.ok("boardId: "+response.getBoardId()+"CommentID: "+response.getId());
    }

    // 댓글 삭제
    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Map<String, Long>> deleteComment(@PathVariable Long commentId) {
        Long userId = authUserService.getAuthenticatedUserId(); // ✅ 로그인된 사용자 정보 가져오기
        Map<String, Long> response = commentService.deleteComment(userId, commentId);
        return ResponseEntity.ok(response);
    }
}
