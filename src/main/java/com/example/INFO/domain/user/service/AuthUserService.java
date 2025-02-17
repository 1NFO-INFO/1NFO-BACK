package com.example.INFO.domain.user.service;

import com.example.INFO.domain.auth.dto.UserDetailsImpl;
import com.example.INFO.domain.user.exception.UserException;
import com.example.INFO.domain.user.exception.UserExceptionType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthUserService {

    public UserDetailsImpl getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserException(UserExceptionType.UNAUTHORIZED_USER);
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetailsImpl) {
            return (UserDetailsImpl) principal;
        } else {
            throw new UserException(UserExceptionType.INTERNAL_SERVER_ERROR);
        }
    }

    public long getAuthenticatedUserId() {
        return getAuthenticatedUser().getId();
    }
}
