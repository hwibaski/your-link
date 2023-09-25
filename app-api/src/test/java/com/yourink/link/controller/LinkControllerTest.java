package com.yourink.link.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yourink.domain.link.Link;
import com.yourink.domain.member.Member;
import com.yourink.dto.api.ErrorCode;
import com.yourink.dto.page.CursorResult;
import com.yourink.exception.NotFoundException;
import com.yourink.link.controller.dto.CreateLinkRequest;
import com.yourink.link.controller.dto.GetLinkListResponse;
import com.yourink.link.controller.dto.GetLinkResponse;
import com.yourink.link.controller.dto.UpdateLinkRequest;
import com.yourink.link.service.LinkReadService;
import com.yourink.link.service.LinkWriteService;
import com.yourink.member.service.MemberReadService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class LinkControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private LinkWriteService linkWriteService;
    @MockBean
    private LinkReadService linkReadService;
    @MockBean
    private MemberReadService memberReadService;

    @Nested
    @DisplayName("링크 생성 테스트")
    class CreateLinkTest {
        private final String testApiPath = "/api/v1/link";

        @Test
        @DisplayName("링크를 생성한다.")
        void createLink_success() throws Exception {
            // given
            String title = "타이틀";
            String linkUrl = "http://www.yourlink.com";
            List<String> tags = List.of("tag1", "tag2");

            var requestDto = new CreateLinkRequest(title, linkUrl, tags);
            var requestBody = objectMapper.writeValueAsString(requestDto);

            Link mockLink = mock(Link.class);
            given(mockLink.getId()).willReturn(1L);
            given(linkWriteService.createLink(any(), any(), any(), any())).willReturn(mockLink);

            // when
            // then
            mockMvc.perform(post(testApiPath)
                                    .contentType(APPLICATION_JSON)
                                    .content(requestBody).param("memberId", "1"))
                   .andExpect(status().isCreated())
                   .andExpect(jsonPath("$.success").value(true))
                   .andExpect(jsonPath("$.message").value("링크가 생성되었습니다."))
                   .andExpect(jsonPath("$.code").value("OK"))
                   .andExpect(jsonPath("$.data.id").isNotEmpty());
        }

        @Test
        @DisplayName("링크의 제목(title)이 빈문자열이면 400 에러를 반환한다.")
        void createLink_title_is_blank() throws Exception {
            // given
            String title = "";
            String linkUrl = "http://www.yourlink.com";
            List<String> tags = List.of("tag1", "tag2");

            var requestDto = new CreateLinkRequest(title, linkUrl, tags);
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
            List<String> tags = List.of("tag1", "tag2");

            var requestDto = new CreateLinkRequest(title, linkUrl, tags);
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
            List<String> tags = List.of("tag1", "tag2");

            var requestDto = new CreateLinkRequest(title, linkUrl, tags);
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
            List<String> tags = List.of("tag1", "tag2");

            var requestDto = new CreateLinkRequest(title, linkUrl, tags);
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

    }


    @Nested
    @DisplayName("링크 수정 테스트")
    class UpdateLinkTest {
        private final String testApiPath = "/api/v1/link";

        @Test
        @DisplayName("링크를 수정한다.")
        void updateLink_success() throws Exception {
            // given
            Member mockMember = mock(Member.class);
            given(mockMember.getId()).willReturn(1L);
            given(memberReadService.getMemberById(any())).willReturn(mockMember);

            Link mockLink = mock(Link.class);
            given(mockLink.getId()).willReturn(1L);
            given(linkWriteService.updateLink(any(), any(), any(), any(), any())).willReturn(mockLink);

            Long id = 1L;
            String titleAfterUpdate = "변경 후 타이틀";
            String linkUrlAfterUpdate = "http://www.linkToUpdate.com";
            List<String> newTags = List.of("tag1", "tag2");

            var requestDto = new UpdateLinkRequest(id, titleAfterUpdate, linkUrlAfterUpdate, newTags);
            var requestBody = objectMapper.writeValueAsString(requestDto);

            // when
            // then
            mockMvc.perform(patch(testApiPath)
                                    .contentType(APPLICATION_JSON)
                                    .content(requestBody)
                                    .param("memberId", "1"))
                   .andExpect(status().isOk())
                   .andExpect(jsonPath("$.success").value(true))
                   .andExpect(jsonPath("$.message").value("링크의 수정이 완료되었습니다"))
                   .andExpect(jsonPath("$.code").value("OK"))
                   .andExpect(jsonPath("$.data.id").isNotEmpty());
        }

        @Test
        @DisplayName("해당하는 id의 링크가 없으면 404 에러를 반환한다.")
        void updateLink_not_found_link() throws Exception {
            // given
            Long id = 1L;
            String titleToUpdate = "변경 요청 타이틀";
            String linkUrlToUpdate = "http://www.linkToUpdate.com";
            List<String> newTags = List.of("tag1", "tag2");

            var requestDto = new UpdateLinkRequest(id, titleToUpdate, linkUrlToUpdate, newTags);
            var requestBody = objectMapper.writeValueAsString(requestDto);

            Member mockMember = mock(Member.class);
            given(mockMember.getId()).willReturn(1L);
            given(memberReadService.getMemberById(any())).willReturn(mockMember);
            given(linkWriteService.updateLink(any(), any(), any(), any(), any()))
                    .willThrow(new NotFoundException(ErrorCode.NOT_FOUND.getMessage(), ErrorCode.NOT_FOUND.getCode(), ErrorCode.NOT_FOUND.getStatus()));

            // when
            // then
            mockMvc.perform(patch(testApiPath)
                                    .contentType(APPLICATION_JSON)
                                    .content(requestBody)
                                    .param("memberId", "1"))
                   .andExpect(status().isNotFound())
                   .andExpect(jsonPath("$.success").value(false))
                   .andExpect(jsonPath("$.message").value("요청한 자원을 찾을 수 없습니다"))
                   .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                   .andExpect(jsonPath("$.data").isEmpty());
        }

        @Test
        @DisplayName("링크의 제목(title)과 url(linkUrl) 모두 유효성 통과를 하지 못하면 400 에러를 반환하고 validation 필드에 에러 메세지가 나간다.")
        void createLink_title_and_link_is_not_valid() throws Exception {
            // given
            String title = "";
            String linkUrl = null;
            List<String> tags = List.of("tag1", "tag2");

            var requestDto = new CreateLinkRequest(title, linkUrl, tags);
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


        @Test
        @DisplayName("링크의 id가 null이면 400 에러를 반환한다.")
        void updateLink_id_is_null() throws Exception {
            // given
            Long id = null;
            String titleToUpdate = "변경 요청 타이틀";
            String linkUrlToUpdate = "http://www.linkToUpdate.com";
            List<String> newTags = List.of("tag1", "tag2");

            var requestDto = new UpdateLinkRequest(id, titleToUpdate, linkUrlToUpdate, newTags);
            var requestBody = objectMapper.writeValueAsString(requestDto);

            // when
            // then
            mockMvc.perform(patch(testApiPath)
                                    .contentType(APPLICATION_JSON)
                                    .content(requestBody))
                   .andExpect(status().isBadRequest())
                   .andExpect(jsonPath("$.success").value(false))
                   .andExpect(jsonPath("$.message").value("잘못된 요청입니다"))
                   .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                   .andExpect(jsonPath("$.validation.id").value("아이디를 확인해주세요"));
        }

        @Test
        @DisplayName("링크의 제목(title)이 빈문자열이면 400 에러를 반환한다.")
        void updateLink_title_is_blank() throws Exception {
            // given
            Long id = 1L;
            String titleToUpdate = "";
            String linkUrlToUpdate = "http://www.linkBeforeUpdate.com";
            List<String> newTags = List.of("tag1", "tag2");

            var requestDto = new UpdateLinkRequest(id, titleToUpdate, linkUrlToUpdate, newTags);
            var requestBody = objectMapper.writeValueAsString(requestDto);

            // when
            // then
            mockMvc.perform(patch(testApiPath)
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
        void updateLink_title_is_null() throws Exception {
            // given
            Long id = 1L;
            String titleToUpdate = null;
            String linkUrlToUpdate = "http://www.linkBeforeUpdate.com";
            List<String> newTags = List.of("tag1", "tag2");

            var requestDto = new UpdateLinkRequest(id, titleToUpdate, linkUrlToUpdate, newTags);
            var requestBody = objectMapper.writeValueAsString(requestDto);

            // when
            // then
            mockMvc.perform(patch(testApiPath)
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
        void updateLink_linkUrl_is_not_url() throws Exception {
            // given
            Long id = 1L;
            String titleToUpdate = "변경 전 타이틀";
            String linkUrlToUpdate = "I am not URL";
            List<String> newTags = List.of("tag1", "tag2");

            var requestDto = new UpdateLinkRequest(id, titleToUpdate, linkUrlToUpdate, newTags);
            var requestBody = objectMapper.writeValueAsString(requestDto);

            // when
            // then
            mockMvc.perform(patch(testApiPath)
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
        void updateLink_linkUrl_is_null() throws Exception {
            // given
            Long id = 1L;
            String titleToUpdate = "변경 전 타이틀";
            String linkUrlToUpdate = null;
            List<String> newTags = List.of("tag1", "tag2");

            var requestDto = new UpdateLinkRequest(id, titleToUpdate, linkUrlToUpdate, newTags);
            var requestBody = objectMapper.writeValueAsString(requestDto);

            // when
            // then
            mockMvc.perform(patch(testApiPath)
                                    .contentType(APPLICATION_JSON)
                                    .content(requestBody))
                   .andExpect(status().isBadRequest())
                   .andExpect(jsonPath("$.success").value(false))
                   .andExpect(jsonPath("$.message").value("잘못된 요청입니다"))
                   .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                   .andExpect(jsonPath("$.validation.linkUrl").value("URL 형식을 확인해주세요"));
        }

        @Test
        @DisplayName("링크의 id, 제목(title) url(linkUrl) 모두 유효성 통과를 하지 못하면 400 에러를 반환하고 validation 필드에 에러 메세지가 나간다.")
        void updateLink_id_title_linkUrl_is_not_valid() throws Exception {
            // given
            Long id = null;
            String titleToUpdate = null;
            String linkUrlToUpdate = null;
            List<String> newTags = List.of("tag1", "tag2");

            var requestDto = new UpdateLinkRequest(id, titleToUpdate, linkUrlToUpdate, newTags);
            var requestBody = objectMapper.writeValueAsString(requestDto);

            // when
            // then
            mockMvc.perform(patch(testApiPath)
                                    .contentType(APPLICATION_JSON)
                                    .content(requestBody))
                   .andExpect(status().isBadRequest())
                   .andExpect(jsonPath("$.success").value(false))
                   .andExpect(jsonPath("$.message").value("잘못된 요청입니다"))
                   .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                   .andExpect(jsonPath("$.validation.id").value("아이디를 확인해주세요"))
                   .andExpect(jsonPath("$.validation.title").value("타이틀을 확인해주세요"))
                   .andExpect(jsonPath("$.validation.linkUrl").value("URL 형식을 확인해주세요"));
        }
    }

    @Nested
    @DisplayName("링크 목록 조회 테스트")
    class GetLinksTest {
        private final String testApiPath = "/api/v1/links";

        @Test
        @DisplayName("링크의 목록을 조회한다.")
        void getLinks_success() throws Exception {
            // given
            List<GetLinkListResponse> collect = LongStream.rangeClosed(1, 10)
                                                          .mapToObj(id -> new GetLinkListResponse(id, "타이틀-" + id, "https://www.naver.com/" + id, List.of("tag" + id)))
                                                          .toList();

            Member mockMember = mock(Member.class);
            given(mockMember.getId()).willReturn(1L);
            given(memberReadService.getMemberById(any())).willReturn(mockMember);

            given(linkReadService.getALlLinksByIdAndMemberIdDesc(any(), any(), any())).willReturn(new CursorResult<>(collect, true));

            // when
            // then
            mockMvc.perform(get(testApiPath)
                                    .param("id", "1")
                                    .param("size", "10")
                                    .param("memberId", "1"))
                   .andExpect(status().isOk())
                   .andExpect(jsonPath("$.success").value(true))
                   .andExpect(jsonPath("$.code").value("OK"))
                   .andExpect(jsonPath("$.message").value("링크 목록 조회가 완료되었습니다."))
                   .andExpect(jsonPath("$.data.data.length()").value(collect.size()))
                   .andExpect(jsonPath("$.data.hasNext").value(true));
        }

        @Test
        @DisplayName("size 파라미터를 보내지 않을 시에는 기본 사이즈 값인 10개 service 메서드의 인자로 넘겨준다.")
        void getLinks_success_when_id_size_parameter_miss() throws Exception {
            // given
            List<GetLinkListResponse> collect = LongStream.rangeClosed(1, 10)
                                                          .mapToObj(id -> new GetLinkListResponse(id, "타이틀-" + id, "https://www.naver.com/" + id, List.of("tag" + id)))
                                                          .toList();

            Member mockMember = mock(Member.class);
            given(mockMember.getId()).willReturn(1L);
            given(memberReadService.getMemberById(any())).willReturn(mockMember);

            given(linkReadService.getALlLinksByIdAndMemberIdDesc(any(), any(), any())).willReturn(new CursorResult<>(collect, true));

            // when
            // then
            mockMvc.perform(get(testApiPath)
                                    .param("id", "1")
                                    .param("memberId", "1")
            );
            verify(linkReadService).getALlLinksByIdAndMemberIdDesc(1L, 10, 1L);
        }

        @Test
        @DisplayName("가지고 있는 링크가 없을 경우 빈 리스트를 리턴한다")
        void getLinks_success_when_link_size_is_zero() throws Exception {
            // given
            Member mockMember = mock(Member.class);
            given(mockMember.getId()).willReturn(1L);
            given(memberReadService.getMemberById(any())).willReturn(mockMember);

            given(linkReadService.getALlLinksByIdAndMemberIdDesc(any(), any(), any())).willReturn(new CursorResult<>(new ArrayList<>(), false));

            // when
            // then
            mockMvc.perform(get(testApiPath)
                                    .param("id", "1")
                                    .param("size", "10")
                                    .param("memberId", "1"))
                   .andExpect(status().isOk())
                   .andExpect(jsonPath("$.success").value(true))
                   .andExpect(jsonPath("$.code").value("OK"))
                   .andExpect(jsonPath("$.message").value("링크 목록 조회가 완료되었습니다."))
                   .andExpect(jsonPath("$.data.data.length()").value(0))
                   .andExpect(jsonPath("$.data.hasNext").value(false));
        }
    }

    @Nested
    @DisplayName("단일 링크 조회 테스트")
    class GetLinkTest {
        private final String testApiPath = "/api/v1/link/{id}";

        @Test
        @DisplayName("링크를 조회한다.")
        void getLink_success() throws Exception {
            // given
            given(linkReadService.getLink(any())).willReturn(new GetLinkResponse(1L, "타이틀", "https://www.naver.com"));

            // when
            // then
            Long linkIdToGet = 1L;
            mockMvc.perform(get(testApiPath, linkIdToGet)
                                    .contentType(APPLICATION_JSON)
                   )
                   .andExpect(status().isOk())
                   .andExpect(jsonPath("$.success").value(true))
                   .andExpect(jsonPath("$.code").value("OK"))
                   .andExpect(jsonPath("$.message").value("링크 조회가 완료되었습니다."))
                   .andExpect(jsonPath("$.data.id").value(1L));
        }

        @Test
        @DisplayName("해당하는 id의 링크가 없을 시 예외를 반환한다.")
        void getLink_when_link_not_found() throws Exception {
            // given
            given(linkReadService.getLink(any()))
                    .willThrow(new NotFoundException(ErrorCode.NOT_FOUND.getMessage(), ErrorCode.NOT_FOUND.getCode(), ErrorCode.NOT_FOUND.getStatus()));

            // when
            // then
            Long linkIdToGet = 1L;
            mockMvc.perform(get(testApiPath, linkIdToGet)
                                    .contentType(APPLICATION_JSON))
                   .andExpect(status().isNotFound())
                   .andExpect(jsonPath("$.success").value(false))
                   .andExpect(jsonPath("$.message").value("요청한 자원을 찾을 수 없습니다"))
                   .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                   .andExpect(jsonPath("$.data").isEmpty());
        }
    }
}
