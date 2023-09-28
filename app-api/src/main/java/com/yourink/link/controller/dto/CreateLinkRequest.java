package com.yourink.link.controller.dto;

import com.yourink.validator.ValidUrl;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CreateLinkRequest(
        @NotBlank(message = "타이틀을 확인해주세요") String title,
        @ValidUrl String linkUrl,
        List<String> tags) {
}
