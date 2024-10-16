package com.yunusemrenalbant.springjwtredis.exception;

public class EmailAlreadyExistsException extends AlreadyException {

    private static final String DEFAULT_MESSAGE =
            "The specified email already exists";

    private static final String MESSAGE_TEMPLATE =
            "Email already exists: ";

    public EmailAlreadyExistsException(String email) {
        super(MESSAGE_TEMPLATE.concat(email));
    }

    public EmailAlreadyExistsException() {
        super(DEFAULT_MESSAGE);
    }
}
