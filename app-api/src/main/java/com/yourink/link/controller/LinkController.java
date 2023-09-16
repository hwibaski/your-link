package com.yourink.link.controller;

import com.yourink.dto.api.ApiResponse;
import com.yourink.link.controller.dto.CreateLinkRequest;
import com.yourink.link.controller.dto.CreateLinkResponse;
import com.yourink.link.service.LinkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LinkController {
    private final LinkService linkService;

    @PostMapping("/api/v1/link")
    public ResponseEntity<ApiResponse<CreateLinkResponse>> createLink(@Valid @RequestBody CreateLinkRequest createLinkRequest) {
        var result = linkService.createLink(createLinkRequest.title(), createLinkRequest.linkUrl());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("링크가 생성되었습니다.", new CreateLinkResponse(result.id(), result.title(), result.linkUrl())));
    }
}
