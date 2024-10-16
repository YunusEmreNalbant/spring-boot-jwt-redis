package com.yunusemrenalbant.springjwtredis.controller;

import com.yunusemrenalbant.springjwtredis.dto.request.LoginRequest;
import com.yunusemrenalbant.springjwtredis.dto.request.SignupRequest;
import com.yunusemrenalbant.springjwtredis.dto.response.CustomResponse;
import com.yunusemrenalbant.springjwtredis.dto.response.JWTResponse;
import com.yunusemrenalbant.springjwtredis.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomResponse<String> register(@RequestBody SignupRequest request) {

        return CustomResponse.created(authService.register(request));
    }

    @PostMapping("/login")
    public CustomResponse<JWTResponse> login(@RequestBody LoginRequest request) {

        return CustomResponse.ok(authService.login(request));
    }

    @PostMapping("/logout")
    public CustomResponse<String> logout(@RequestHeader("Authorization") String token) {

        return CustomResponse.ok(authService.logout(token));
    }
}
