package com.yourink.member.controller.dto;

import jakarta.validation.constraints.Email;

public record CreateMemberRequest(
        @Email(message = "이메일을 확인해주세요") String email) {
}
