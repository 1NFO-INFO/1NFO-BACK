package com.example.INFO.domain.board.domain.repository;

import com.example.INFO.domain.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
