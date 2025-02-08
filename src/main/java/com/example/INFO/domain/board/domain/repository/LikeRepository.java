package com.example.INFO.domain.board.domain.repository;

import com.example.INFO.domain.board.domain.Board;
import com.example.INFO.domain.board.domain.Comment;
import com.example.INFO.domain.board.domain.Like;
import com.example.INFO.domain.user.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByUserAndBoard(UserEntity user, Board board);
    boolean existsByUserAndComment(UserEntity user, Comment comment);

    Optional<Like> findByUserIdAndBoardId(Long userId, Long boardId);
    Optional<Like> findByUserIdAndCommentId(Long userId, Long commentId);
}
