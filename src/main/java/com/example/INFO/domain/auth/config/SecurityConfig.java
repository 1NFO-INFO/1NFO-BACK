package com.example.INFO.domain.auth.config;

import com.example.INFO.domain.auth.config.filter.JwtTokenFilter;
import com.example.INFO.domain.user.exception.CustomAuthenticationEntryPoint;
import com.example.INFO.domain.auth.service.CustomUserDetailsService;
import com.example.INFO.domain.auth.service.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String[] SWAGGER_URLS = {
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**"
    };

    private static final String[] PUBLIC_GET_URLS = {
            "/api/v1/oauth/kakao/authorize",
            "/api/v1/oauth/kakao/callback",
            "/api/v1/boards/search/**",
            "/api/v1/boards/category",
            "/api/v1/comments/search/board/**",
            "/api/v1/comments/**",
            "/api/v1/cheongyak/**"
    };

    private static final String[] PUBLIC_POST_URLS = {
            "/api/v1/auth",
            "/api/v1/auth/login",
            "/api/v1/auth/refresh"
    };

    private static final String[] AUTHENTICATED_URLS = {
            "/api/v1/favorites/**",
            "/api/v1/users/me",
    };

    private final JwtTokenService jwtTokenService;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(SWAGGER_URLS).permitAll()
                                .requestMatchers(HttpMethod.POST, PUBLIC_POST_URLS).permitAll()
                                .requestMatchers(HttpMethod.GET, PUBLIC_GET_URLS).permitAll()
                                .requestMatchers("/api/v1/ticket/**").permitAll()
                                .requestMatchers(AUTHENTICATED_URLS).authenticated()
                                .anyRequest().permitAll()
                ).sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(new JwtTokenFilter(jwtTokenService, userDetailsService), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                )
        ;

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
