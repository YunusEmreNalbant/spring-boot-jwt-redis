package com.yunusemrenalbant.springjwtredis.exception;

import org.springframework.http.HttpStatus;

public abstract class ProcessException extends RuntimeException {

    public static final HttpStatus STATUS = HttpStatus.INTERNAL_SERVER_ERROR;

    protected ProcessException(String message) {
        super(message);
    }
}
