package com.yourink.member.controller;

import com.yourink.dto.api.ApiResponse;
import com.yourink.member.controller.dto.CreateMemberRequest;
import com.yourink.member.controller.dto.CreateMemberResponse;
import com.yourink.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/api/v1/member")
    public ResponseEntity<ApiResponse<CreateMemberResponse>> createMember(@Valid @RequestBody CreateMemberRequest createMemberRequest) {
        var result = memberService.createMember(createMemberRequest.email());

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(ApiResponse.success("멤버가 생성되었습니다.", new CreateMemberResponse(result.id(), result.email())));
    }
}
