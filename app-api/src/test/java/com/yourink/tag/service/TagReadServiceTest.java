package com.yourink.tag.service;

import com.yourink.domain.link.Link;
import com.yourink.domain.tag.Tag;
import com.yourink.domain.tag.TagLinkMap;
import com.yourink.repository.link.LinkRepository;
import com.yourink.repository.tag.TagLinkMapRepository;
import com.yourink.repository.tag.TagRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class TagReadServiceTest {
    @Autowired
    private TagReadService tagReadService;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TagLinkMapRepository tagLinkMapRepository;

    @Autowired
    private LinkRepository linkRepository;

    @AfterEach
    void tearDown() {
        tagRepository.deleteAllInBatch();
    }

    @Nested
    @DisplayName("특정 링크와 연결된 태그 조회 테스트")
    class getTagListByLinkTest {
        @Test
        @DisplayName("특정 링크와 연결된 태그를 조회한다.")
        void get_tags_by_connected_link_by_id() {
            // given
            var savedLink = linkRepository.save(Link.create("타이틀", "https://www.naver.com"));
            var savedTag = tagRepository.save(Tag.create("태그1"));
            tagLinkMapRepository.save(TagLinkMap.create(savedLink, savedTag));

            // when
            var result = tagReadService.getTagsByLink(savedLink.getId());

            // then
            assertThat(result.tags()).containsExactly("태그1");
        }

        @Test
        @DisplayName("주어진 id에 해당하는 링크가 없으면 빈 리스트를 반환한다.")
        void temp() {
            // given
            var notExistLinkId = 1L;

            // when
            var result = tagReadService.getTagsByLink(notExistLinkId);

            // then
            assertThat(result.tags()).isEmpty();
        }
    }
}
