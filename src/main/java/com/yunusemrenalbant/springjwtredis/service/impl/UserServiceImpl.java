package com.yunusemrenalbant.springjwtredis.service.impl;

import com.yunusemrenalbant.springjwtredis.model.User;
import com.yunusemrenalbant.springjwtredis.repository.UserRepository;
import com.yunusemrenalbant.springjwtredis.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }
}
