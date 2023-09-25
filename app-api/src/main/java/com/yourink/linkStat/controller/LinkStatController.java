package com.yourink.linkStat.controller;

import com.yourink.dto.api.ApiResponse;
import com.yourink.linkStat.controller.dto.GetLinkCountResponse;
import com.yourink.linkStat.service.LinkStatReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LinkStatController {
    private final LinkStatReadService linkStatReadService;

    @GetMapping("/api/v1/link/count")
    public ResponseEntity<ApiResponse<GetLinkCountResponse>> getLinkCount() {
        var result = linkStatReadService.countAll();

        return ResponseEntity.ok(ApiResponse.success("링크 수 조회가 완료되었습니다.", result));
    }
}
