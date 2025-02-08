package com.example.INFO.domain.favorite.domain.repository;

import com.example.INFO.domain.favorite.domain.Favorite;
import com.example.INFO.domain.user.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUser(UserEntity user);
}
