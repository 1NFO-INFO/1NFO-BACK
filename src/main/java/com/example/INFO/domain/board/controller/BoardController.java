package com.example.INFO.domain.board.controller;

import com.example.INFO.domain.board.dto.req.BoardCreateRequest;
import com.example.INFO.domain.board.dto.req.BoardUpdateRequest;
import com.example.INFO.domain.board.dto.res.BoardResponse;
import com.example.INFO.domain.board.service.BoardService;
import com.example.INFO.domain.s3service.S3ImageService;
import com.example.INFO.domain.user.service.AuthUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/boards")
@RequiredArgsConstructor
@Tag(name = "게시글 API", description = "게시글 관련 API")
public class BoardController {
    private final BoardService boardService; //board service
    private final S3ImageService s3ImageService; //사진 저장
    private final AuthUserService authUserService; //현재 로그인된 사용자 정보를 가져오기

    // 게시글 작성
    @Operation(summary = "게시글 작성", description = "이미지 업로드 포함하여 게시글을 작성합니다.")
    @ApiResponse(responseCode = "201", description = "게시글 생성 성공")
    @PostMapping(value="/create",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createBoard(
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestPart("request") @Valid BoardCreateRequest request) {

        // 현재 로그인된 사용자 ID 가져오기
        Long userId = authUserService.getAuthenticatedUserId();
        //이미지 받기 / 없으면 null
        String imageUrl = (image != null && !image.isEmpty()) ? s3ImageService.upload(image) : null;

        // 게시글 생성 요청에 업로드된 이미지 URL을 추가
        boardService.createBoard(userId, request, imageUrl);

        return ResponseEntity.status(HttpStatus.CREATED).body("Board created successfully");
    }
    // 게시글 삭제
    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBoard(@PathVariable Long id) {
        Long userId = authUserService.getAuthenticatedUserId(); // 현재 로그인한 사용자 ID 가져오기
        boardService.deleteBoard(id, userId);
        return ResponseEntity.ok("Board deleted successfully");
    }
    // 단일 게시글 조회
    @Operation(summary = "단일 게시글 조회", description = "게시글 ID로 특정 게시글을 조회합니다.")
    @GetMapping("/search/{id}")
    public ResponseEntity<BoardResponse> getBoard(@PathVariable Long id) {
        BoardResponse response = boardService.getBoard(id);
        return ResponseEntity.ok(response);
    }

    // 모든 게시글 조회
    @Operation(summary = "모든 게시글 조회", description = "모든 게시글을 조회합니다.")
    @GetMapping("/search")
    public ResponseEntity<List<BoardResponse>> getAllBoards() {
        List<BoardResponse> responses = boardService.getAllBoards();
        return ResponseEntity.ok(responses);
    }
    // 게시글 수정
    @Operation(summary = "게시글 수정", description = "게시글 내용을 수정합니다. (이미지 선택 가능)")
    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BoardResponse> updateBoard(
            @PathVariable Long id,
            @RequestPart("request") @Valid BoardUpdateRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        Long userId = authUserService.getAuthenticatedUserId(); // 현재 로그인한 사용자 ID 가져오기
        BoardResponse response = boardService.updateBoard(id, userId, request, image);
        return ResponseEntity.ok(response);
    }
}
