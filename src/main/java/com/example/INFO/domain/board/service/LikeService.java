package com.example.INFO.domain.board.service;

import com.example.INFO.domain.board.domain.Board;
import com.example.INFO.domain.board.domain.Like;
import com.example.INFO.domain.board.domain.repository.BoardRepository;
import com.example.INFO.domain.board.domain.repository.CommentRepository;
import com.example.INFO.domain.board.domain.repository.LikeRepository;
import com.example.INFO.domain.board.dto.res.LikeResponse;
import com.example.INFO.domain.user.model.entity.UserEntity;
import com.example.INFO.domain.user.repository.UserRepository;
import com.example.INFO.global.exception.DefaultException;
import com.example.INFO.global.exception.NotFoundException;
import com.example.INFO.global.payload.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    // 게시글 좋아요
    @Transactional
    public LikeResponse likeBoard(Long userId, Long boardId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND.getCode(), "User not found"));
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND.getCode(), "Board not found"));

        // 중복 좋아요 방지
        if (likeRepository.existsByUserAndBoard(user, board)) {
            throw new DefaultException(ErrorCode.DUPLICATE_ERROR, "Already liked");
        }

        Like like = Like.builder()
                .user(user)
                .board(board)
                .build();

        likeRepository.save(like);

        return LikeResponse.builder()
                .boardId(boardId)
                .likeCount(board.getLikeCount()) // 현재 좋아요 개수 반환
                .build();
    }
}