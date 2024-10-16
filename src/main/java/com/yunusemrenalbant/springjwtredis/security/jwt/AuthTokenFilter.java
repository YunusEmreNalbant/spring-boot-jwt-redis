package com.yunusemrenalbant.springjwtredis.security.jwt;

import com.yunusemrenalbant.springjwtredis.security.CustomUserDetailsService;
import com.yunusemrenalbant.springjwtredis.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService customUserDetailsService;
    private final TokenService tokenService;

    private static final Logger log = LoggerFactory.getLogger(AuthTokenFilter.class);

    public AuthTokenFilter(JwtUtils jwtUtils, CustomUserDetailsService customUserDetailsService, TokenService tokenService) {
        this.jwtUtils = jwtUtils;
        this.customUserDetailsService = customUserDetailsService;
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {

            String jwt = parseJwt(request);
            log.error("AuthTokenFilter | doFilterInternal | jwt: {}", jwt);

            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {

                String email = jwtUtils.getEmailFromToken(jwt);

                String redisToken = tokenService.getTokenFromRedis(email);

                if (redisToken == null || !redisToken.equals(jwt)) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Invalid token");

                    log.error("AuthTokenFilter | doFilterInternal | Unauthorized: Invalid token");
                }


                UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            log.error("AuthTokenFilter | doFilterInternal | Cannot set user authentication: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {

        String headerAuth = request.getHeader("Authorization");

        log.info("AuthTokenFilter | parseJwt | headerAuth: {}", headerAuth);

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {

            log.info("AuthTokenFilter | parseJwt | parseJwt: {}", headerAuth.substring(7));

            return headerAuth.substring(7);
        }

        return null;
    }
}
