package com.example.INFO.domain.board.service;

import com.example.INFO.domain.board.domain.Board;
import com.example.INFO.domain.board.domain.Comment;
import com.example.INFO.domain.board.domain.Like;
import com.example.INFO.domain.board.domain.repository.BoardRepository;
import com.example.INFO.domain.board.domain.repository.CommentRepository;
import com.example.INFO.domain.board.domain.repository.LikeRepository;
import com.example.INFO.domain.board.dto.res.CommentLikeResponse;
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

    // 게시글 좋아요 취소
    @Transactional
    public LikeResponse unlikeBoard(Long userId, Long boardId) {
        Like like = likeRepository.findByUserIdAndBoardId(userId, boardId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND.getCode(), "Like not found"));

        likeRepository.delete(like);
        likeRepository.flush();

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND.getCode(), "Board not found"));

        return LikeResponse.builder()
                .boardId(boardId)
                .likeCount(board.getLikeCount())
                .build();
    }
    // 댓글 좋아요
    @Transactional
    public CommentLikeResponse likeComment(Long userId, Long commentId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND.getCode(), "User not found"));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND.getCode(), "Comment not found"));

        // 중복 좋아요 방지
        if (likeRepository.existsByUserAndComment(user, comment)) {
            throw new DefaultException(ErrorCode.DUPLICATE_ERROR, "Already liked");
        }

        Like like = Like.builder()
                .user(user)
                .comment(comment)
                .build();

        likeRepository.save(like);

        return CommentLikeResponse.builder()
                .commentId(commentId)
                .likeCount(comment.getLikeCount()) // 현재 좋아요 개수 반환
                .build();
    }
    // 댓글 좋아요 취소
    @Transactional
    public CommentLikeResponse unlikeComment(Long userId, Long commentId) {
        Like like = likeRepository.findByUserIdAndCommentId(userId, commentId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND.getCode(), "Like not found"));

        likeRepository.delete(like);
        likeRepository.flush();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND.getCode(), "Comment not found"));

        return CommentLikeResponse.builder()
                .commentId(commentId)
                .likeCount(comment.getLikeCount())
                .build();
    }

}