package com.yourink.dto.api;

import lombok.Getter;

@Getter
public enum ErrorCode {
    BAD_REQUEST(400, "BAD_REQUEST", "잘못된 요청입니다"),
    NOT_FOUND(404, "NOT_FOUND", "요청한 자원을 찾을 수 없습니다");

    final int status;

    final String code;

    final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
