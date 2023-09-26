package com.yourink.tag.controller;

import com.yourink.dto.api.ApiResponse;
import com.yourink.tag.controller.dto.GetTagListByLinkResponse;
import com.yourink.tag.service.TagReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TagController {
    private final TagReadService tagReadService;

    @GetMapping("/api/v1/tags")
    public ResponseEntity<ApiResponse<GetTagListByLinkResponse>> getTagsByLink(@RequestParam Long linkId) {
        var result = tagReadService.getTagsByLink(linkId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("태그 목록 조회가 완료되었습니다.", result));
    }
}
