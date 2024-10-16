package com.yunusemrenalbant.springjwtredis.service.impl;

import com.yunusemrenalbant.springjwtredis.dto.request.LoginRequest;
import com.yunusemrenalbant.springjwtredis.dto.request.SignupRequest;
import com.yunusemrenalbant.springjwtredis.dto.response.JWTResponse;
import com.yunusemrenalbant.springjwtredis.exception.EmailAlreadyExistsException;
import com.yunusemrenalbant.springjwtredis.exception.UserNotFoundException;
import com.yunusemrenalbant.springjwtredis.model.User;
import com.yunusemrenalbant.springjwtredis.repository.UserRepository;
import com.yunusemrenalbant.springjwtredis.security.jwt.JwtUtils;
import com.yunusemrenalbant.springjwtredis.service.AuthService;
import com.yunusemrenalbant.springjwtredis.service.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final TokenService tokenService;

    public AuthServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, AuthenticationManager authenticationManager, JwtUtils jwtUtils, TokenService tokenService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.tokenService = tokenService;
    }


    @Override
    public String register(SignupRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }


        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(request.getRole())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);

        return "success";
    }

    @Override
    public JWTResponse login(LoginRequest request) {

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwtToken = jwtUtils.generateJwtToken(auth);

        userRepository.findByEmail(request.getEmail()).orElseThrow(UserNotFoundException::new);

        tokenService.saveTokenToRedis(request.getEmail(), jwtToken, Duration.ofDays(1));

        return JWTResponse.builder()
                .email(request.getEmail())
                .token(jwtToken)
                .build();
    }

    @Override
    public String logout(String token) {
        String authToken = jwtUtils.extractTokenFromHeader(token);

        if (jwtUtils.validateJwtToken(authToken)) {
            String email = jwtUtils.getEmailFromToken(authToken);

            tokenService.deleteTokenFromRedis(email);

            return "success";
        }


        return "failed";
    }
}
