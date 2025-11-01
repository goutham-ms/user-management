package com.merchant.user_onboarding.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    private static final long serialVersionUID = 3L;

    public UserAlreadyExistsException () {
    }

    public UserAlreadyExistsException (String msg) {
        super(msg);
    }
}