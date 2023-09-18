package com.yourink.link.controller.dto;

import jakarta.validation.constraints.NotNull;

public record GetLinkRequest(@NotNull(message = "아이디를 확인해주세요") Long id) {
}
