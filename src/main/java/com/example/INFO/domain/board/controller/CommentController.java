package com.example.INFO.domain.board.controller;

import com.example.INFO.domain.board.dto.req.CommentCreateRequest;
import com.example.INFO.domain.board.dto.res.CommentResponse;
import com.example.INFO.domain.board.service.CommentService;
import com.example.INFO.domain.user.service.AuthUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Tag(name = "댓글 API", description = "댓글 관련 API")
public class CommentController {
    private final CommentService commentService;
    private final AuthUserService authUserService;

    // 댓글 생성
    @PostMapping("/create")
    @Operation(summary = "댓글 작성", description = "게시글에 댓글을 작성합니다.")
    public ResponseEntity<CommentResponse> createComment(@RequestBody CommentCreateRequest request) {
        Long userId = authUserService.getAuthenticatedUserId(); // ✅ 로그인된 사용자 정보 가져오기
        CommentResponse response = commentService.createComment(userId, request);
        return ResponseEntity.ok(response);
    }
}
