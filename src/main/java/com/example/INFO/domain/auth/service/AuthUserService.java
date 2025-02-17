package com.example.INFO.domain.auth.service;

import com.example.INFO.domain.auth.dto.UserDetailsImpl;
import com.example.INFO.global.exception.DefaultException;
import com.example.INFO.global.payload.ErrorCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthUserService {

    public UserDetailsImpl getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new DefaultException(ErrorCode.UNAUTHORIZED);
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetailsImpl) {
            return (UserDetailsImpl) principal;
        } else {
            throw new DefaultException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public long getAuthenticatedUserId() {
        return getAuthenticatedUser().getId();
    }
}
