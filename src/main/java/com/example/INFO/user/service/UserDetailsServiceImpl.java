package com.example.INFO.user.service;

import com.example.INFO.user.dto.UserDetailsImpl;
import com.example.INFO.user.exception.UserException;
import com.example.INFO.user.exception.UserExceptionType;
import com.example.INFO.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLocalAuthDetailsUsername(username)
                .map(UserDetailsImpl::fromEntity)
                .orElseThrow(() -> new UserException(UserExceptionType.USER_NOT_FOUND));
    }
}
