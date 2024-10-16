package com.yunusemrenalbant.springjwtredis.service;

import com.yunusemrenalbant.springjwtredis.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findById(Long userId);
}
