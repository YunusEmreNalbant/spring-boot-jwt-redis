package com.yunusemrenalbant.springjwtredis.service;

import java.time.Duration;

public interface TokenService {
    void saveTokenToRedis(String email, String jwtToken, Duration duration);
    String getTokenFromRedis(String email);
    void deleteTokenFromRedis(String email);
}
