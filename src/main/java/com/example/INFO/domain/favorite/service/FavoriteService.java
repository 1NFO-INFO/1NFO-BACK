package com.example.INFO.domain.favorite.service;

import com.example.INFO.domain.favorite.domain.Favorite;
import com.example.INFO.domain.favorite.domain.repository.FavoriteRepository;
import com.example.INFO.domain.favorite.dto.res.FavoriteResponseDto;
import com.example.INFO.domain.favorite.dto.res.PagedResponseDto;
import com.example.INFO.domain.ticket.domain.TicketData;
import com.example.INFO.domain.ticket.domain.repository.TicketDataRepository;
import com.example.INFO.domain.user.model.entity.UserEntity;
import com.example.INFO.domain.user.repository.UserRepository;
import com.example.INFO.domain.auth.service.AuthUserService;
import com.example.INFO.global.exception.ConflictException;
import com.example.INFO.global.exception.NotFoundException;
import com.example.INFO.global.payload.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final TicketDataRepository ticketDataRepository;
    private final UserRepository userRepository;
    private final AuthUserService authUserService;

    // 좋아요 추가
    public FavoriteResponseDto likeEntity(String entityId, String entityType) {
        UserEntity user = findAuthenticatedUser();

        if (favoriteRepository.findByUserAndEntityIdAndEntityType(user, entityId, entityType).isPresent()) {
            throw new ConflictException(ErrorCode.DUPLICATE_ERROR, "이미 좋아요한 항목입니다.");
        }

        Favorite favorite = favoriteRepository.save(Favorite.builder()
                .user(user)
                .entityId(entityId)
                .entityType(entityType)
                .build());

        return mapToResponseDto(favorite);
    }

    // 좋아요 취소
    public void unlikeEntity(String entityId, String entityType) {
        UserEntity user = findAuthenticatedUser();

        Favorite favorite = favoriteRepository.findByUserAndEntityIdAndEntityType(user, entityId, entityType)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND, "좋아요하지 않은 항목입니다."));

        favoriteRepository.delete(favorite);
    }

    // ✅ 좋아요한 항목 조회 (페이징 처리)
    public PagedResponseDto<FavoriteResponseDto> getFavoriteEntities(Pageable pageable) {
        UserEntity user = findAuthenticatedUser();

        Page<FavoriteResponseDto> favoritePage = favoriteRepository.findByUser(user, pageable)
                .map(this::mapToResponseDto);

        return new PagedResponseDto<>(
                favoritePage.getContent(),
                favoritePage.getNumber(),
                favoritePage.getSize(),
                favoritePage.getTotalElements(),
                favoritePage.getTotalPages(),
                favoritePage.isLast()
        );
    }

    //------------------------- 예외처리 메소드 -------------------------

    // 사용자 조회 메서드
    private UserEntity findAuthenticatedUser() {
        long userId = authUserService.getAuthenticatedUserId();
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }

    // Favorite → FavoriteResponseDto 변환 메서드
    private FavoriteResponseDto mapToResponseDto(Favorite favorite) {
        String title = null;

        // 좋아요한 항목이 `TICKET`일 경우
        if ("TICKET".equals(favorite.getEntityType())) {
            TicketData ticket = ticketDataRepository.findById(favorite.getEntityId())
                    .orElse(null);
            if (ticket != null) {
                title = ticket.getTitle();
            }
        }

        return new FavoriteResponseDto(
                favorite.getId(),
                favorite.getEntityId(),
                favorite.getEntityType(),
                title,
                favorite.getCreatedTime()
        );
    }
}

