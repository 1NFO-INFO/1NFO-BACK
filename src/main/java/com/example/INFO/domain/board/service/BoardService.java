package com.example.INFO.domain.board.service;

import com.example.INFO.domain.board.domain.Board;
import com.example.INFO.domain.board.domain.Category;
import com.example.INFO.domain.board.domain.repository.BoardRepository;
import com.example.INFO.domain.board.dto.req.BoardCreateRequest;
import com.example.INFO.domain.board.dto.req.BoardUpdateRequest;
import com.example.INFO.domain.board.dto.res.BoardPageResponse;
import com.example.INFO.domain.board.dto.res.BoardResponse;
import com.example.INFO.domain.board.dto.res.BoardSimpleResponse;
import com.example.INFO.domain.board.dto.res.TopLikedBoardResponse;
import com.example.INFO.domain.s3service.S3ImageService;
import com.example.INFO.domain.user.model.entity.UserEntity;
import com.example.INFO.domain.user.repository.UserRepository;
import com.example.INFO.global.exception.DefaultException;
import com.example.INFO.global.exception.NotFoundException;
import com.example.INFO.global.exception.UnauthorizedException;
import com.example.INFO.global.payload.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final S3ImageService s3ImageService;
    // 게시글 생성
    @Transactional
    public void createBoard(Long userId, BoardCreateRequest request, String imageUrl) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND.getCode(), "User not found"));

        Board board = Board.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .category_name(request.getCategoryName())
                .post_image(imageUrl)
                .user(user)
                .build();

        boardRepository.save(board);
    }
    // 게시글 삭제
    public void deleteBoard(Long id, Long userId) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND.getCode(), "Board not found"));
        //작성자 확인
        if (!board.getUser().getId().equals(userId)) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED.getCode(), "User not authorized to delete this board");
        }

        boardRepository.delete(board);
    }

    // 단일 게시글 조회
    @Transactional(readOnly = true)
    public BoardResponse getBoard(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND.getCode(), "Board not found"));

        return convertToBoardResponse(board);
    }

    // 모든 게시글 조회
    @Transactional(readOnly = true)
    public List<BoardResponse> getAllBoards() {
        List<Board> boards = boardRepository.findAll();
        return boards.stream()
                .map(this::convertToBoardResponse)
                .collect(Collectors.toList());
    }

    // 게시글 수정
    @Transactional
    public BoardResponse updateBoard(Long id, Long userId, BoardUpdateRequest request, MultipartFile image) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND.getCode(), "Board not found"));

        if (!board.getUser().getId().equals(userId)) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED.getCode(), "User not authorized to update this board");
        }

        // 기존 이미지 URL 유지
        String imageUrl = board.getPost_image();

        // 이미지가 새로 업로드된 경우만 S3 업로드 수행
        if (image != null && !image.isEmpty()) {
            try {
                System.out.println("🖼️ 새로운 이미지 업로드 시작...");
                imageUrl = s3ImageService.upload(image); // S3에 업로드
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("S3 업로드 실패");
            }
        }

        // 게시글 정보 업데이트
        board.update(request.getTitle(), request.getContent(), request.getCategoryName(), imageUrl);

        return convertToBoardResponse(board);
    }
    //페이징 카테고리
    @Transactional(readOnly = true)
    public BoardPageResponse getBoardsByCategory(String categoryName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));

        Category categoryEnum;
        try {
            categoryEnum = Category.valueOf(categoryName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new DefaultException(ErrorCode.INVALID_PARAMETER, "Invalid category name");
        }

        Page<Board> boardPage = boardRepository.findByCategory(categoryEnum, pageable);
        return convertToBoardPageResponse(boardPage);
    }
    // 좋아요 많은 상위 3개 게시글
    @Transactional(readOnly = true)
    public List<TopLikedBoardResponse> getTop3BoardsByLikes() {
        Pageable pageable = PageRequest.of(0, 3);
        List<Board> boards = boardRepository.findTop3ByLikesCount(pageable);

        return boards.stream()
                .map(this::convertToTopLikedBoardResponse)
                .collect(Collectors.toList());
    }
    //좋아요 갱신
    @Transactional(readOnly = true)
    public List<TopLikedBoardResponse> getTop3LikedBoardsFromYesterday() {
        ZoneId zoneId = ZoneId.of("Asia/Seoul");
        LocalDateTime start = LocalDate.now(zoneId).minusDays(1).atStartOfDay();
        LocalDateTime end = start.plusDays(1).minusNanos(1);

        Pageable pageable = PageRequest.of(0, 3);
        List<Board> topBoards = boardRepository.findTop3ByLikes(start, end, pageable);

        log.info("🔥 어제 좋아요 기준 인기 게시글 조회 기간: {} ~ {}", start, end);
        log.info("📌 조회된 게시글 개수: {}", topBoards.size());

        return topBoards.stream()
                .map(this::convertToTopLikedBoardResponse)
                .collect(Collectors.toList());
    }

    // Board → BoardResponse 변환
    private BoardResponse convertToBoardResponse(Board board) {
        return BoardResponse.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .categoryName(board.getCategory_name().getDisplayName())
                .postImage(board.getPost_image())
                .createdTime(board.getCreatedTime())
                .updatedTime(board.getUpdatedTime())
                .likeCount(board.getLikes().size())
                .build();
    }
    // Page<Board> → BoardPageResponse 변환
    private BoardPageResponse convertToBoardPageResponse(Page<Board> boardPage) {
        return BoardPageResponse.builder()
                .content(boardPage.getContent().stream()
                        .map(this::convertToBoardSimpleResponse)
                        .collect(Collectors.toList()))
                .currentPage(boardPage.getNumber())
                .totalPages(boardPage.getTotalPages())
                .totalElements(boardPage.getTotalElements())
                .last(boardPage.isLast())
                .build();
    }
    // Board → BoardSimpleResponse 변환
    private BoardSimpleResponse convertToBoardSimpleResponse(Board board) {
        return BoardSimpleResponse.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .likeCount(board.getLikeCount())
                .commentCount(board.getComments().size())
                .build();
    }
    // Board → TopLikedBoardResponse 변환
    private TopLikedBoardResponse convertToTopLikedBoardResponse(Board board) {
        return TopLikedBoardResponse.builder()
                .title(board.getTitle())
                .category(board.getCategory_name().name())
                .createdTime(board.getCreatedTime())
                .updatedTime(board.getUpdatedTime())
                .build();
    }
}
