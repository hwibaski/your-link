package com.yourink.exception;

import lombok.Getter;

@Getter
public abstract class CustomException extends RuntimeException {
    private final String code;
    private final int status;

    public CustomException(String message, String code, int status) {
        super(message);
        this.code = code;
        this.status = status;
    }
}
