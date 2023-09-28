package com.yourink.auth.controller;

import com.yourink.auth.controller.dto.LoginRequest;
import com.yourink.auth.controller.dto.LoginResponse;
import com.yourink.dto.api.ApiResponse;
import com.yourink.member.service.MemberReadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final MemberReadService memberReadService;

    @PostMapping("/api/v1/auth/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        var result = memberReadService.getMemberByEmail(loginRequest.email());
        
        return ResponseEntity.status(HttpStatus.OK)
                             .body(ApiResponse.success("로그인이 완료되었습니다", new LoginResponse(result.getId())));
    }
}
