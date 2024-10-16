package com.yunusemrenalbant.springjwtredis.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/secret")
public class SecretController {

    @GetMapping
    public String secret() {
        return "secret";
    }
}
