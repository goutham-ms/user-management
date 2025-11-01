package com.merchant.user_onboarding.exceptions;

public class DateNotValidException extends RuntimeException {
    private static final long serialVersionUID = 2L;

    public DateNotValidException () {
    }

    public DateNotValidException (String msg) {
        super(msg);
    }
}
