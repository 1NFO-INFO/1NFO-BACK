package com.example.INFO.domain.board.service;

import com.example.INFO.domain.board.domain.Board;
import com.example.INFO.domain.board.domain.repository.BoardRepository;
import com.example.INFO.domain.board.dto.req.BoardCreateRequest;
import com.example.INFO.domain.board.dto.res.BoardResponse;
import com.example.INFO.domain.s3service.S3ImageService;
import com.example.INFO.domain.user.model.entity.UserEntity;
import com.example.INFO.domain.user.repository.UserRepository;
import com.example.INFO.global.exception.NotFoundException;
import com.example.INFO.global.exception.UnauthorizedException;
import com.example.INFO.global.payload.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
