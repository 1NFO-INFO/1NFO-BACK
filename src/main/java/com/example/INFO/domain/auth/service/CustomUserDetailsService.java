package com.example.INFO.domain.auth.service;

import com.example.INFO.domain.auth.dto.UserDetailsImpl;
import com.example.INFO.domain.user.repository.UserRepository;
import com.example.INFO.global.exception.DefaultException;
import com.example.INFO.global.payload.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService {

    private final UserRepository userRepository;

    public UserDetails loadUserByUserId(long userId) throws UsernameNotFoundException {
        return userRepository.findById(userId)
                .map(UserDetailsImpl::fromEntity)
                .orElseThrow(() -> new DefaultException(ErrorCode.NOT_FOUND));
    }
}
