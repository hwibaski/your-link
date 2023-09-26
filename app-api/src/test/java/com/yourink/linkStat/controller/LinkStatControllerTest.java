package com.yourink.linkStat.controller;

import com.yourink.linkStat.controller.dto.GetLinkCountResponse;
import com.yourink.linkStat.service.LinkStatReadService;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class LinkStatControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LinkStatReadService linkStatReadService;

    @Nested
    @DisplayName("링크 통계 조회 테스트")
    class LinkStatReadTest {
        private final String testApiPath = "/api/v1/link/count";

        @Test
        @DisplayName("링크 통계를 조회한다.")
        void read_link_stat() throws Exception {
            // given
            given(linkStatReadService.countAll()).willReturn(new GetLinkCountResponse(1L));

            // when
            // then
            mockMvc.perform(get(testApiPath)
                            .contentType(APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("링크 수 조회가 완료되었습니다."))
                    .andExpect(jsonPath("$.code").value("OK"))
                    .andExpect(jsonPath("$.data.count").value(1))
                    .andDo(document("get-link-count",
                            Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                            Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                            responseFields(
                                    fieldWithPath("success").description("응답 성공 여부"),
                                    fieldWithPath("message").description("응답 메시지"),
                                    fieldWithPath("code").description("응답 코드"),
                                    fieldWithPath("validation").description("유효성 검사 결과"),
                                    fieldWithPath("data.count").description("링크 수")
                            )
                    ));
        }
    }
}
