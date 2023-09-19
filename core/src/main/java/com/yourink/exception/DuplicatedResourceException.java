package com.yourink.exception;

public class DuplicatedResourceException extends CustomException {
    public DuplicatedResourceException(String message, String code, int status) {
        super(message, code, status);
    }
}
