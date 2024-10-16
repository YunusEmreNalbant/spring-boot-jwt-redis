package com.yunusemrenalbant.springjwtredis.service.impl;

import com.yunusemrenalbant.springjwtredis.service.TokenService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class TokenServiceImpl implements TokenService {

    private final StringRedisTemplate stringRedisTemplate;

    public TokenServiceImpl(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void saveTokenToRedis(String email, String jwtToken, Duration duration) {
        String redisKey = "token:" + email;

        stringRedisTemplate.opsForValue().set(redisKey, jwtToken, duration);
    }

    @Override
    public String getTokenFromRedis(String email) {
        String redisKey = "token:" + email;

        return stringRedisTemplate.opsForValue().get(redisKey);
    }

    @Override
    public void deleteTokenFromRedis(String email) {
        String redisKey = "token:" + email;

        stringRedisTemplate.delete(redisKey);
    }
}
