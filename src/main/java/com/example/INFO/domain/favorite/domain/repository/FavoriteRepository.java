package com.example.INFO.domain.favorite.domain.repository;

import com.example.INFO.domain.favorite.domain.Favorite;
import com.example.INFO.domain.ticket.domain.TicketData;
import com.example.INFO.domain.user.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findByUserAndEntityIdAndEntityType(UserEntity user, String entityId, String entityType);
    List<Favorite> findByUserOrderByCreatedTimeDesc(UserEntity user); // ✅ 최신순 정렬
}
