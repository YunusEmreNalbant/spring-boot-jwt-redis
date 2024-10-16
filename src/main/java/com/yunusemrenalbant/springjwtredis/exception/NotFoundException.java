package com.yunusemrenalbant.springjwtredis.exception;

import org.springframework.http.HttpStatus;

public abstract class NotFoundException extends RuntimeException {

    public static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

    protected NotFoundException(String message) {
        super(message);
    }
}
