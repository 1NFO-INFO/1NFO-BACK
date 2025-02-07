package com.example.INFO.user.service;

import com.example.INFO.user.dto.UserDetailsImpl;
import com.example.INFO.user.exception.UserException;
import com.example.INFO.user.exception.UserExceptionType;
import com.example.INFO.user.repository.UserRepository;
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
                .orElseThrow(() -> new UserException(UserExceptionType.USER_NOT_FOUND));
    }
}
