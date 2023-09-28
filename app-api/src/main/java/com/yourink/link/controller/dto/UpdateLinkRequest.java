package com.yourink.link.controller.dto;

import com.yourink.validator.ValidUrl;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UpdateLinkRequest(
        @NotNull(message = "아이디를 확인해주세요") Long id,
        @NotBlank(message = "타이틀을 확인해주세요") String title,
        @ValidUrl String linkUrl,
        List<String> tags) {
}
