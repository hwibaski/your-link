package com.yourink.link.controller;

import com.yourink.dto.api.ApiResponse;
import com.yourink.dto.page.CursorPageSearch;
import com.yourink.dto.page.CursorResult;
import com.yourink.link.controller.dto.CreateLinkRequest;
import com.yourink.link.controller.dto.CreateLinkResponse;
import com.yourink.link.controller.dto.GetLinkListResponse;
import com.yourink.link.controller.dto.GetLinkResponse;
import com.yourink.link.controller.dto.UpdateLinkRequest;
import com.yourink.link.controller.dto.UpdateLinkResponse;
import com.yourink.link.service.LinkReadService;
import com.yourink.link.service.LinkWriteService;
import com.yourink.member.service.MemberReadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
            @Valid @RequestBody UpdateLinkRequest updateLinkRequest, @RequestParam("memberId") Long memberId) {
        var member = memberReadService.getMemberById(memberId);
        var result = linkWriteService.updateLink(updateLinkRequest.id(), updateLinkRequest.title(), updateLinkRequest.linkUrl(), updateLinkRequest.tags(), member.getId());

        return ResponseEntity.status(HttpStatus.OK)
                             .body(ApiResponse.success("링크의 수정이 완료되었습니다", new UpdateLinkResponse(result.getId())));
    }

    @GetMapping("/api/v1/links")
    public ResponseEntity<ApiResponse<CursorResult<GetLinkListResponse>>> getLinks(
            @ModelAttribute CursorPageSearch cursorPageSearch, @RequestParam("memberId") Long memberId) {
        var member = memberReadService.getMemberById(memberId);
        var result = linkReadService.getALlLinksByIdAndMemberIdDesc(cursorPageSearch.id(), cursorPageSearch.size(), member.getId());

        return ResponseEntity.status(HttpStatus.OK)
                             .body(ApiResponse.success("링크 목록 조회가 완료되었습니다.", result));
    }

    @GetMapping("/api/v1/link/{id}")
    public ResponseEntity<ApiResponse<GetLinkResponse>> getLink(
            @PathVariable("id") Long id, @RequestParam("memberId") Long memberId) {
        var result = linkReadService.getLink(id, memberId);

        return ResponseEntity.status(HttpStatus.OK)
                             .body(ApiResponse.success("링크 조회가 완료되었습니다.", result));
    }
}
