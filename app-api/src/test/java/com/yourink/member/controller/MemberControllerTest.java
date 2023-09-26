package com.yourink.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yourink.dto.member.MemberResponse;
import com.yourink.member.controller.dto.CreateMemberRequest;
import com.yourink.member.service.MemberWriteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private MemberWriteService memberWriteService;

    @Nested
    @DisplayName("멤버 생성 테스트")
    class CreateMemberTest {
        private final String testApiPath = "/api/v1/member";

        @Test
        @DisplayName("멤버를 생성한다")
        void createMember_success() throws Exception {
            // given
            String email = "temp@gmail.com";

            var requestDto = new CreateMemberRequest(email);
            var requestBody = objectMapper.writeValueAsString(requestDto);

            given(memberWriteService.createMember(any())).willReturn(new MemberResponse(1L, email));

            // when
            // then
            mockMvc.perform(post(testApiPath).contentType(APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("멤버가 생성되었습니다."))
                    .andExpect(jsonPath("$.code").value("OK"))
                    .andExpect(jsonPath("$.data.id").isNotEmpty())
                    .andExpect(jsonPath("$.validation").isEmpty())
                    .andDo(document("create-member",
                            Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                            Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                            requestFields(
                                    fieldWithPath("email").description("멤버 이메일").attributes(new Attributes.Attribute("constraints", "이메일 형식"))
                            ),
                            responseFields(
                                    fieldWithPath("success").description("응답 성공 여부"),
                                    fieldWithPath("message").description("응답 메시지"),
                                    fieldWithPath("code").description("응답 코드"),
                                    fieldWithPath("data.id").description("멤버 아이디"),
                                    fieldWithPath("validation").description("유효성 검사 결과")
                            ))
                    );
        }

        @Test
        @DisplayName("이메일의 형식이 맞지 않으면 400 에러를 반환한다.")
        void createMember_email_is_not_valid() throws Exception {
            // given
            String email = "temp";

            var requestDto = new CreateMemberRequest(email);
            var requestBody = objectMapper.writeValueAsString(requestDto);

            // when
            // then
            mockMvc.perform(post(testApiPath).contentType(APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value("잘못된 요청입니다"))
                    .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                    .andExpect(jsonPath("$.data").isEmpty())
                    .andExpect(jsonPath("$.validation.email").value("이메일을 확인해주세요"));
        }
    }
}
