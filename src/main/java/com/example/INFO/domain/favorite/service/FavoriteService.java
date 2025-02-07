package com.example.INFO.domain.favorite.service;

import com.example.INFO.domain.favorite.domain.Favorite;
import com.example.INFO.domain.favorite.domain.repository.FavoriteRepository;
import com.example.INFO.domain.ticket.domain.TicketData;
import com.example.INFO.domain.ticket.domain.repository.TicketDataRepository;
import com.example.INFO.domain.user.exception.UserException;
import com.example.INFO.domain.user.exception.UserExceptionType;
import com.example.INFO.domain.user.model.entity.UserEntity;
import com.example.INFO.domain.user.repository.UserRepository;
import com.example.INFO.domain.user.service.AuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final TicketDataRepository ticketDataRepository;
    private final UserRepository userRepository;
    private final AuthUserService authUserService;

    //티켓 좋아요 메소드
    public void likeTicket(String ticketSeq) {
        long userId = authUserService.getAuthenticatedUserId();
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserExceptionType.USER_NOT_FOUND));

        TicketData ticket = ticketDataRepository.findById(ticketSeq)
                .orElseThrow(() -> new IllegalArgumentException("해당 티켓이 존재하지 않습니다."));

        favoriteRepository.findByUserAndTicket(user, ticket)
                .ifPresent(favorite -> {
                    throw new IllegalStateException("이미 좋아요한 티켓입니다.");
                });

        favoriteRepository.save(Favorite.builder()
                .user(user)
                .ticket(ticket)
                .build());
    }
}