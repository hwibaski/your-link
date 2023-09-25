package com.yourink.link.controller;

import com.yourink.dto.api.ApiResponse;
import com.yourink.dto.page.CursorPageSearch;
import com.yourink.dto.page.CursorResult;
import com.yourink.link.controller.dto.*;
import com.yourink.link.service.LinkReadService;
import com.yourink.link.service.LinkWriteService;
import com.yourink.member.service.MemberReadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LinkController {
    private final LinkWriteService linkWriteService;
    private final LinkReadService linkReadService;
    private final MemberReadService memberReadService;

    @PostMapping("/api/v1/link")
    public ResponseEntity<ApiResponse<CreateLinkResponse>> createLink(
            @Valid @RequestBody CreateLinkRequest createLinkRequest, @RequestParam("memberId") Long memberId) {
        var member = memberReadService.getMemberById(memberId);
        var result = linkWriteService.createLink(createLinkRequest.title(), createLinkRequest.linkUrl(), createLinkRequest.tags(), member);

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(ApiResponse.success("링크가 생성되었습니다.", new CreateLinkResponse(result.getId())));
    }

    @PatchMapping("/api/v1/link")
    public ResponseEntity<ApiResponse<UpdateLinkResponse>> updateLink(
            @Valid @RequestBody UpdateLinkRequest updateLinkRequest) {
        var result = linkWriteService.updateLink(updateLinkRequest.id(), updateLinkRequest.title(), updateLinkRequest.linkUrl(), updateLinkRequest.tags());

        return ResponseEntity.status(HttpStatus.OK)
                             .body(ApiResponse.success("링크의 수정이 완료되었습니다", new UpdateLinkResponse(result.getId())));
    }

    @GetMapping("/api/v1/links")
    public ResponseEntity<ApiResponse<CursorResult<GetLinkListResponse>>> getLinks(
            @ModelAttribute CursorPageSearch cursorPageSearch) {
        var result = linkReadService.getALlLinksByIdDesc(cursorPageSearch.id(), cursorPageSearch.size());

        return ResponseEntity.status(HttpStatus.OK)
                             .body(ApiResponse.success("링크 목록 조회가 완료되었습니다.", result));
    }

    @GetMapping("/api/v1/link/{id}")
    public ResponseEntity<ApiResponse<GetLinkResponse>> getLink(
            @PathVariable("id") Long id) {
        var result = linkReadService.getLink(id);

        return ResponseEntity.status(HttpStatus.OK)
                             .body(ApiResponse.success("링크 조회가 완료되었습니다.", result));
    }
}
