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
import com.example.INFO.global.exception.ConflictException;
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
        UserEntity user = validateUserExists(userId);
        Board board = validateBoardExists(boardId);

        validateAlreadyLikedBoard(user, board);

        Like like = Like.builder()
                .user(user)
                .board(board)
                .build();

        likeRepository.save(like);

        return LikeResponse.builder()
                .boardId(boardId)
                .likeId(like.getLikeId()) // Like ID 반환
                .likeCount(board.getLikeCount())
                .build();
    }

    // 게시글 좋아요 취소
    @Transactional
    public LikeResponse unlikeBoard(Long userId, Long boardId) {
        Like like = validateLikeExistsForBoard(userId, boardId);
        Long likeId = like.getLikeId(); // 삭제 전 Like ID 저장

        likeRepository.delete(like);
        likeRepository.flush();

        Board board = validateBoardExists(boardId);

        return LikeResponse.builder()
                .boardId(boardId)
                .likeId(likeId) // 삭제한 Like ID 반환
                .likeCount(board.getLikeCount())
                .build();
    }

    // 댓글 좋아요
    @Transactional
    public CommentLikeResponse likeComment(Long userId, Long commentId) {
        UserEntity user = validateUserExists(userId);
        Comment comment = validateCommentExists(commentId);

        validateAlreadyLikedComment(user, comment);

        Like like = Like.builder()
                .user(user)
                .comment(comment)
                .build();

        likeRepository.save(like);

        return CommentLikeResponse.builder()
                .commentId(commentId)
                .likeId(like.getLikeId()) // Like ID 반환
                .likeCount(comment.getLikeCount())
                .build();
    }

    // 댓글 좋아요 취소
    @Transactional
    public CommentLikeResponse unlikeComment(Long userId, Long commentId) {
        Like like = validateLikeExistsForComment(userId, commentId);
        Long likeId = like.getLikeId(); // 삭제 전 Like ID 저장

        likeRepository.delete(like);
        likeRepository.flush();

        Comment comment = validateCommentExists(commentId);

        return CommentLikeResponse.builder()
                .commentId(commentId)
                .likeId(likeId) // 삭제한 Like ID 반환
                .likeCount(comment.getLikeCount())
                .build();
    }
    //------------------------------예외처리 메소드------------------------------------------

    // 사용자 존재 여부 확인
    private UserEntity validateUserExists(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND, "User not found"));
    }

    // 게시글 존재 여부 확인
    private Board validateBoardExists(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND, "Board not found"));
    }

    // 댓글 존재 여부 확인
    private Comment validateCommentExists(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND, "Comment not found"));
    }

    // 해당 사용자가 이미 게시글에 좋아요를 눌렀는지 확인
    private void validateAlreadyLikedBoard(UserEntity user, Board board) {
        if (likeRepository.existsByUserAndBoard(user, board)) {
            throw new ConflictException(ErrorCode.DUPLICATE_ERROR, "Already liked this board");
        }
    }

    // 해당 사용자가 이미 댓글에 좋아요를 눌렀는지 확인
    private void validateAlreadyLikedComment(UserEntity user, Comment comment) {
        if (likeRepository.existsByUserAndComment(user, comment)) {
            throw new ConflictException(ErrorCode.DUPLICATE_ERROR, "Already liked this comment");
        }
    }

    // 특정 사용자의 게시글 좋아요 여부 확인
    private Like validateLikeExistsForBoard(Long userId, Long boardId) {
        return likeRepository.findByUserIdAndBoardId(userId, boardId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND, "Like not found"));
    }

    // 특정 사용자의 댓글 좋아요 여부 확인
    private Like validateLikeExistsForComment(Long userId, Long commentId) {
        return likeRepository.findByUserIdAndCommentId(userId, commentId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND, "Like not found"));
    }

}
