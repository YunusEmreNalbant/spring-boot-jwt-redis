package com.yunusemrenalbant.springjwtredis.exception;

public class UserNotFoundException extends NotFoundException {

    private static final String DEFAULT_MESSAGE =
            "The specified user is not found";

    private static final String MESSAGE_TEMPLATE =
            "No user was found with ID: ";

    public UserNotFoundException(Long id) {
        super(MESSAGE_TEMPLATE.concat(String.valueOf(id)));
    }

    public UserNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}

