package com.yourink.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yourink.auth.controller.dto.LoginRequest;
import com.yourink.domain.member.Member;
import com.yourink.dto.api.ErrorCode;
import com.yourink.exception.NotFoundException;
import com.yourink.member.service.MemberReadService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private MemberReadService memberReadService;

    @Nested
    @DisplayName("로그인 테스트")
    class LoginTest {
        private final String testApiPath = "/api/v1/auth/login";

        @Test
        @DisplayName("로그인 성공")
        void login_success() throws Exception {
            // given
            Member mockMember = mock(Member.class);
            given(mockMember.getId()).willReturn(1L);
            given(mockMember.getEmail()).willReturn("temp@gmail.com");
            given(memberReadService.getMemberByEmail(any())).willReturn(mockMember);

            var requestDto = new LoginRequest("temp@gmail.com");
            var requestBody = objectMapper.writeValueAsString(requestDto);

            // when
            // then
            mockMvc.perform(post(testApiPath)
                                    .contentType(APPLICATION_JSON)
                                    .content(requestBody)
                   )
                   .andExpect(status().isOk())
                   .andExpect(jsonPath("$.success").value(true))
                   .andExpect(jsonPath("$.code").value("OK"))
                   .andExpect(jsonPath("$.message").value("로그인이 완료되었습니다"))
                   .andExpect(jsonPath("$.data.id").value(1L))
                   .andDo(document("login",
                                   Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                                   Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                                   requestFields(
                                           fieldWithPath("email").description("로그인할 이메일")
                                   ),
                                   responseFields(
                                           fieldWithPath("success").description("응답 성공 여부"),
                                           fieldWithPath("message").description("응답 메세지"),
                                           fieldWithPath("code").description("응답 코드"),
                                           fieldWithPath("validation").description("유효성 검사 결과"),
                                           fieldWithPath("data.id").description("멤버의 id")
                                   )));
        }

        @Test
        @DisplayName("로그인 실패 - 이메일이 존재하지 않음")
        void login_fail_email_not_found() throws Exception {
            // given
            given(memberReadService.getMemberByEmail(any()))
                    .willThrow(new NotFoundException(ErrorCode.NOT_FOUND.getMessage(), ErrorCode.NOT_FOUND.getCode(), ErrorCode.NOT_FOUND.getStatus()));

            var requestDto = new LoginRequest("temp@gmail.com");
            var requestBody = objectMapper.writeValueAsString(requestDto);

            // when
            // then
            mockMvc.perform(post(testApiPath)
                                    .contentType(APPLICATION_JSON)
                                    .content(requestBody)
                   )
                   .andExpect(status().isNotFound())
                   .andExpect(jsonPath("$.success").value(false))
                   .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                   .andExpect(jsonPath("$.message").value("요청한 자원을 찾을 수 없습니다"))
                   .andExpect(jsonPath("$.data").isEmpty());
        }

        @Test
        @DisplayName("로그인 실패 - 요청한 이메일 형식이 아님")
        void login_fail_email_not_valid() throws Exception {
            // given
            String email = "temp";

            var requestDto = new LoginRequest(email);
            var requestBody = objectMapper.writeValueAsString(requestDto);

            // when
            // then
            mockMvc.perform(post(testApiPath)
                                    .contentType(APPLICATION_JSON)
                                    .content(requestBody)
                   )
                   .andExpect(status().isBadRequest())
                   .andExpect(jsonPath("$.success").value(false))
                   .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                   .andExpect(jsonPath("$.message").value("잘못된 요청입니다"))
                   .andExpect(jsonPath("$.data").isEmpty())
                   .andExpect(jsonPath("$.validation.email").value("이메일을 확인해주세요"));
        }
    }
}
