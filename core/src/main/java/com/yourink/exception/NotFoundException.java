package com.yourink.exception;

public class NotFoundException extends CustomException {
    public NotFoundException(String message, String code, int status) {
        super(message, code, status);
    }
}
