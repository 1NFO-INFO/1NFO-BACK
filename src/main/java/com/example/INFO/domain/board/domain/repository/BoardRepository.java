package com.example.INFO.domain.board.domain.repository;

import com.example.INFO.domain.board.domain.Board;
import com.example.INFO.domain.board.domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    //카테고리별 정렬 및 페이징
    @Query("SELECT b FROM Board b WHERE b.category_name = :categoryName")
    Page<Board> findByCategory(@Param("categoryName") Category categoryName, Pageable pageable);

    //hot 게시물 3개 가져오기
    @Query("SELECT b FROM Board b LEFT JOIN b.likes l GROUP BY b ORDER BY COUNT(l) DESC")
    List<Board> findTop3ByLikesCount(Pageable pageable);

    //좋아요 갱신
    @Query("SELECT b FROM Board b " +
            "LEFT JOIN b.likes l " +  // ✅ LEFT JOIN으로 변경: 좋아요가 없는 게시글도 포함
            "WHERE b.createdTime BETWEEN :start AND :end " + // ✅ 게시글 작성 날짜 기준 필터링
            "GROUP BY b " +
            "ORDER BY COUNT(l) DESC") // ✅ 좋아요 개수가 많은 순으로 정렬
    List<Board> findTop3ByLikes(@Param("start") LocalDateTime start,
                                @Param("end") LocalDateTime end,
                                Pageable pageable);

}
