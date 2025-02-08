package com.example.INFO.domain.favorite.domain.repository;

import com.example.INFO.domain.favorite.domain.Favorite;
import com.example.INFO.domain.ticket.domain.TicketData;
import com.example.INFO.domain.user.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Page<Favorite> findByUser(UserEntity user, Pageable pageable);
    Optional<Favorite> findByUserAndTicket(UserEntity user, TicketData ticket);

}
