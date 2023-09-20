package com.yourink.link.controller.dto;

import java.util.List;

public record GetLinkResponse(Long id, String title, String linkUrl, List<String> tags) {
}
