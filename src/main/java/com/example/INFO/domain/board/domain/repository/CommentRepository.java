package com.example.INFO.domain.board.domain.repository;

import com.example.INFO.domain.board.domain.Board;
import com.example.INFO.domain.board.domain.Comment;
import com.example.INFO.domain.user.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBoardIdAndParentIsNull(Long boardId);
    List<Comment> findByParentId(Long parentId);
    @Query("SELECT COUNT(l) > 0 FROM Like l WHERE l.user = :user AND l.board = :board")
    boolean existsByUserAndBoard(@Param("user") UserEntity user, @Param("board") Board board);

    @Query("SELECT COUNT(l) > 0 FROM Like l WHERE l.user = :user AND l.comment = :comment")
    boolean existsByUserAndComment(@Param("user") UserEntity user, @Param("comment") Comment comment);

}
