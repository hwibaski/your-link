package com.yourink.link.controller.dto;

import java.util.List;

public record GetLinkListResponse(Long id, String title, String linkUrl, List<String> tags) {
}
