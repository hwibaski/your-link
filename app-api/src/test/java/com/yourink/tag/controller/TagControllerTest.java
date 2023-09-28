package com.yourink.tag.controller;

import com.yourink.tag.controller.dto.GetTagListByLinkResponse;
import com.yourink.tag.service.TagReadService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TagReadService tagReadService;

    @Nested
    @DisplayName("특정 링크와 연결된 태그 조회 테스트")
    class GetTagsByLinkTest {
        private final String testApiPath = "/api/v1/tags";

        @Test
        @DisplayName("특정 링크와 연결된 태그를 조회한다.")
        void get_tags_by_connected_link_by_id() throws Exception {
            // given
            given(tagReadService.getTagsByLink(any())).willReturn(new GetTagListByLinkResponse(List.of("tag1", "tag2")));

            // when
            // then
            var linkId = 1L;
            mockMvc.perform(get(testApiPath)
                            .contentType(APPLICATION_JSON)
                            .param("linkId", String.valueOf(linkId))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("태그 목록 조회가 완료되었습니다."))
                    .andExpect(jsonPath("$.code").value("OK"))
                    .andExpect(jsonPath("$.data.tags[0]").value("tag1"))
                    .andExpect(jsonPath("$.data.tags[1]").value("tag2"))
                    .andDo(document("get-tags-by-link",
                            Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                            Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                            queryParameters(
                                    parameterWithName("linkId").description("링크 ID")
                            ),
                            responseFields(
                                    fieldWithPath("success").description("응답 성공 여부"),
                                    fieldWithPath("message").description("응답 메시지"),
                                    fieldWithPath("code").description("응답 코드"),
                                    fieldWithPath("validation").description("유효성 검사 결과"),
                                    fieldWithPath("data.tags").description("태그 목록")
                            )));
        }

        @Test
        @DisplayName("id에 해당하는 링크가 없으면 빈 리스트를 반환한다.")
        void get_tags_by_connected_link_by_id_when_link_not_found() throws Exception {
            // given
            given(tagReadService.getTagsByLink(any())).willReturn(new GetTagListByLinkResponse(List.of()));

            // when
            // then
            var linkId = 1L;
            mockMvc.perform(get(testApiPath, linkId)
                            .contentType(APPLICATION_JSON)
                            .param("linkId", String.valueOf(linkId))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("태그 목록 조회가 완료되었습니다."))
                    .andExpect(jsonPath("$.code").value("OK"))
                    .andExpect(jsonPath("$.data.tags").isEmpty());
        }
    }
}
