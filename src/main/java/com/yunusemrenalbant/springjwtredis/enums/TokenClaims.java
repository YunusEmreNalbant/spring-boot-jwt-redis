package com.yunusemrenalbant.springjwtredis.enums;

public enum TokenClaims {

    JWT_ID("jti"),
    TYPE("typ"),
    SUBJECT("sub"),
    ROLES("roles"),
    ID("id"),
    EMAIL("email"),
    FIRST_NAME("firstName"),
    LAST_NAME("lastName"),
    ISSUED_AT("iat"),
    EXPIRES_AT("exp");

    private final String value;

    TokenClaims(String firstName) {
        value = firstName;
    }

    public String getValue() {
        return value;
    }
}
