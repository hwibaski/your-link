package com.yourink.auth.controller.dto;

import jakarta.validation.constraints.Email;

public record LoginRequest(@Email(message = "이메일을 확인해주세요") String email) {
}
