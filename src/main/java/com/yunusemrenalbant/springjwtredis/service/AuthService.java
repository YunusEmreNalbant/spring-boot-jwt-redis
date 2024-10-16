package com.yunusemrenalbant.springjwtredis.service;

import com.yunusemrenalbant.springjwtredis.dto.request.LoginRequest;
import com.yunusemrenalbant.springjwtredis.dto.request.SignupRequest;
import com.yunusemrenalbant.springjwtredis.dto.response.JWTResponse;

public interface AuthService {

    String register(SignupRequest request);

    JWTResponse login(LoginRequest request);

    String logout(String token);
}
