package com.masters.chat.filter;

import static com.masters.chat.constants.JwtConstants.AUTHORIZATION_TOKEN_HEADER;
import static com.masters.chat.util.JwtUtils.createAuth;

import java.io.IOException;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtExtractor jwtExtractor;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (request.getHeader(AUTHORIZATION_TOKEN_HEADER) != null) {
            try {
                var jwt = jwtExtractor.apply(request);
                authenticate(jwt);
            } catch (Exception e) {
                throw new BadCredentialsException("Invalid token received {}", e);
            }
        }
        filterChain.doFilter(request, response);
    }

    private void authenticate(Jwt jwt) {
        SecurityContextHolder.getContext().setAuthentication(createAuth(jwt));
    }

}

