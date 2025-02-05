package com.example.INFO.domain.board.domain.repository;

import com.example.INFO.domain.board.domain.Board;
import com.example.INFO.domain.board.domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Long> {
    //카테고리별 정렬 및 페이징
    @Query("SELECT b FROM Board b WHERE b.category_name = :categoryName")
    Page<Board> findByCategory(@Param("categoryName") Category categoryName, Pageable pageable);
}
