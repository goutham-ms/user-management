package com.merchant.user_onboarding.exceptions;

public class AgeNotValidException  extends RuntimeException {
    private static final long serialVersionUID = 4L;

    public AgeNotValidException () {
    }

    public AgeNotValidException (String msg) {
        super(msg);
    }
}