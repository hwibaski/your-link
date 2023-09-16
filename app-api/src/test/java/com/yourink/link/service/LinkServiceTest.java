package com.yourink.link.service;

import com.yourink.domain.link.Link;
import com.yourink.exception.NotFoundException;
import com.yourink.repository.link.LinkRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class LinkServiceTest {
    @Autowired
    private LinkService linkService;

    @Autowired
    private LinkRepository linkRepository;

    @AfterEach
    void tearDown() {
        linkRepository.deleteAllInBatch();
    }

    @DisplayName("링크 생성 테스트")
    @Test
    void create_link() {
        // given
        String title = "타이틀";
        String linkUrl = "https://www.naver.com";

        // when
        var result = linkService.createLink(title, linkUrl);

        // then
        assertThat(result.id()).isNotNull();
        assertThat(result.title()).isEqualTo("타이틀");
        assertThat(result.linkUrl()).isEqualTo("https://www.naver.com");
    }

    @Nested
    @DisplayName("링크 수정 테스트")
    class UpdateLinkTest {
        @DisplayName("링크를 수정한다.")
        @Test
        void update_link_success() {
            // given
            String titleBeforeUpdate = "변경 전 타이틀";
            String linkUrlBeforeUpdate = "https://www.naver.com";

            var linkToSave = new Link(titleBeforeUpdate, linkUrlBeforeUpdate);
            var savedLink = linkRepository.save(linkToSave);

            // when
            String titleAfterUpdate = "변경 후 타이틀";
            String linkUrlAfterUpdate = "https://www.google.com";
            var result = linkService.updateLink(savedLink.getId(), titleAfterUpdate, linkUrlAfterUpdate);

            // then
            assertThat(result.title()).isEqualTo("변경 후 타이틀");
            assertThat(result.linkUrl()).isEqualTo("https://www.google.com");
        }

        @DisplayName("수정하고자 하는 링크가 없을 경우 예외를 발생시킨다.")
        @Test
        void update_link_when_not_found() {
            // given
            // when

            // then
            Long id = 1L;
            String titleAfterUpdate = "변경 후 타이틀";
            String linkUrlAfterUpdate = "https://www.google.com";

            assertThatThrownBy(() -> linkService.updateLink(id, titleAfterUpdate, linkUrlAfterUpdate))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("요청한 자원을 찾을 수 없습니다");
        }
    }
}
