package com.yourink.link.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yourink.link.controller.dto.CreateLinkRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class LinkControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private final String testApiPath = "/api/v1/link";

    @Test
    @DisplayName("링크를 생성한다.")
    void createLink_success() throws Exception {
        // given
        String title = "타이틀";
        String linkUrl = "http://www.yourlink.com";

        var requestDto = new CreateLinkRequest(title, linkUrl);
        var requestBody = objectMapper.writeValueAsString(requestDto);

        // when
        // then
        mockMvc.perform(post(testApiPath)
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("링크가 생성되었습니다."))
                .andExpect(jsonPath("$.code").value("OK"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.title").value(title))
                .andExpect(jsonPath("$.data.linkUrl").value(linkUrl));
    }

    @Test
    @DisplayName("링크의 제목(title)이 빈문자열이면 400 에러를 반환한다.")
    void createLink_title_is_blank() throws Exception {
        // given
        String title = "";
        String linkUrl = "http://www.yourlink.com";

        var requestDto = new CreateLinkRequest(title, linkUrl);
        var requestBody = objectMapper.writeValueAsString(requestDto);

        // when
        // then
        mockMvc.perform(post(testApiPath)
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다"))
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.validation.title").value("타이틀을 확인해주세요"));
    }

    @Test
    @DisplayName("링크의 제목(title)이 null 이면 400 에러를 반환한다.")
    void createLink_title_is_null() throws Exception {
        // given
        String title = null;
        String linkUrl = "http://www.yourlink.com";

        var requestDto = new CreateLinkRequest(title, linkUrl);
        var requestBody = objectMapper.writeValueAsString(requestDto);

        // when
        // then
        mockMvc.perform(post(testApiPath)
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다"))
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.validation.title").value("타이틀을 확인해주세요"));
    }

    @Test
    @DisplayName("링크의 url(linkUrl)이 URL 형식이 아니면 400 에러를 반환한다.")
    void createLink_linkUrl_is_not_url() throws Exception {
        // given
        String title = "타이틀";
        String linkUrl = "url 형식이 아닌 문자열";

        var requestDto = new CreateLinkRequest(title, linkUrl);
        var requestBody = objectMapper.writeValueAsString(requestDto);

        // when
        // then
        mockMvc.perform(post(testApiPath)
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다"))
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.validation.linkUrl").value("URL 형식을 확인해주세요"));
    }

    @Test
    @DisplayName("링크의 url(linkUrl)이 URL이 null이면 400 에러를 반환한다.")
    void createLink_linkUrl_is_null() throws Exception {
        // given
        String title = "타이틀";
        String linkUrl = null;

        var requestDto = new CreateLinkRequest(title, linkUrl);
        var requestBody = objectMapper.writeValueAsString(requestDto);

        // when
        // then
        mockMvc.perform(post(testApiPath)
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다"))
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.validation.linkUrl").value("URL 형식을 확인해주세요"));
    }

    @Test
    @DisplayName("링크의 제목(title)과 url(linkUrl) 모두 유효성 통과를 하지 못하면 400 에러를 반환하고 validation 필드에 에러 메세지가 나간다.")
    void createLink_title_and_link_is_not_valid() throws Exception {
        // given
        String title = "";
        String linkUrl = null;

        var requestDto = new CreateLinkRequest(title, linkUrl);
        var requestBody = objectMapper.writeValueAsString(requestDto);

        // when
        // then
        mockMvc.perform(post(testApiPath)
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다"))
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.validation.title").value("타이틀을 확인해주세요"))
                .andExpect(jsonPath("$.validation.linkUrl").value("URL 형식을 확인해주세요"));
    }
}
