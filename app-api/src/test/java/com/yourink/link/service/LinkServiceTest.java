package com.yourink.link.service;

import com.yourink.repository.link.LinkRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

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
}
