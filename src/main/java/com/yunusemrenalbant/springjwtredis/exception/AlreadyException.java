package com.yunusemrenalbant.springjwtredis.exception;

import org.springframework.http.HttpStatus;

public abstract class AlreadyException extends RuntimeException {

    public static final HttpStatus STATUS = HttpStatus.CONFLICT;

    protected AlreadyException(String message) {
        super(message);
    }
}