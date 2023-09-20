package com.yourink.link.controller;

import com.yourink.dto.api.ApiResponse;
import com.yourink.dto.link.LinkResponse;
import com.yourink.dto.page.CursorPageSearch;
import com.yourink.dto.page.CursorResult;
import com.yourink.link.controller.dto.*;
import com.yourink.link.service.LinkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LinkController {
    private final LinkService linkService;

    @PostMapping("/api/v1/link")
    public ResponseEntity<ApiResponse<CreateLinkResponse>> createLink(
            @Valid @RequestBody CreateLinkRequest createLinkRequest) {
        var result = linkService.createLink(createLinkRequest.title(), createLinkRequest.linkUrl(), createLinkRequest.tags());

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(ApiResponse.success("링크가 생성되었습니다.", new CreateLinkResponse(result.id(), result.title(), result.linkUrl())));
    }

    @PatchMapping("/api/v1/link")
    public ResponseEntity<ApiResponse<UpdateLinkResponse>> updateLink(
            @Valid @RequestBody UpdateLinkRequest updateLinkRequest) {
        var result = linkService.updateLink(updateLinkRequest.id(), updateLinkRequest.title(), updateLinkRequest.linkUrl());

        return ResponseEntity.status(HttpStatus.OK)
                             .body(ApiResponse.success("링크의 수정이 완료되었습니다", new UpdateLinkResponse(result.id(), result.title(), result.linkUrl())));
    }

    @GetMapping("/api/v1/links")
    public ResponseEntity<ApiResponse<CursorResult<LinkResponse>>> getLinks(
            @ModelAttribute CursorPageSearch cursorPageSearch) {
        var result = linkService.getALlLinksByIdDesc(cursorPageSearch.id(), cursorPageSearch.size());

        return ResponseEntity.status(HttpStatus.OK)
                             .body(ApiResponse.success("링크 목록 조회가 완료되었습니다.", result));
    }

    @GetMapping("/api/v1/link")
    public ResponseEntity<ApiResponse<GetLinkResponse>> getLink(
            @Valid @RequestBody GetLinkRequest getLinkRequest) {
        var result = linkService.getLink(getLinkRequest.id());

        return ResponseEntity.status(HttpStatus.OK)
                             .body(ApiResponse.success("링크 조회가 완료되었습니다.", new GetLinkResponse(result.id(), result.title(), result.linkUrl())));
    }
}
