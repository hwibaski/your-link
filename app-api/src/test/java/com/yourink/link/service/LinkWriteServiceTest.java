package com.yourink.link.service;

import com.yourink.domain.link.Link;
import com.yourink.domain.tag.Tag;
import com.yourink.domain.tag.TagLinkMap;
import com.yourink.exception.NotFoundException;
import com.yourink.repository.link.LinkQueryDslRepository;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@SpringBootTest
class LinkWriteServiceTest {
    @Autowired
    private LinkWriteService linkService;

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TagLinkMapRepository tagLinkMapRepository;

    @Autowired
    private LinkQueryDslRepository linkQueryDslRepository;

    @AfterEach
    void tearDown() {
        linkRepository.deleteAllInBatch();
    }

    @Nested
    @DisplayName("링크 생성 테스트")
    class CreateLinkTest {
        @DisplayName("링크를 생성한다.")
        @Test
        void create_link() {
            // given
            String title = "타이틀";
            String linkUrl = "https://www.naver.com";
            List<String> tags = List.of("태그1", "태그2");

            // when
            var result = linkService.createLink(title, linkUrl, tags);

            // then
            assertThat(result.getId()).isNotNull();
            assertThat(result.getTitle()).isEqualTo("타이틀");
            assertThat(result.getLinkUrl()).isEqualTo("https://www.naver.com");
            assertThat(result.getTags()).extracting("name").containsExactlyInAnyOrder("태그1", "태그2");
        }
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

            var linkToSave = Link.create(titleBeforeUpdate, linkUrlBeforeUpdate);
            var savedLink = linkRepository.save(linkToSave);

            // when
            String titleAfterUpdate = "변경 후 타이틀";
            String linkUrlAfterUpdate = "https://www.google.com";
            List<String> newTags = List.of("tag1", "tag2");
            var result = linkService.updateLink(savedLink.getId(), titleAfterUpdate, linkUrlAfterUpdate, newTags);

            // then
            assertThat(result.getTitle()).isEqualTo(titleAfterUpdate);
            assertThat(result.getLinkUrl()).isEqualTo(linkUrlAfterUpdate);
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
            List<String> newTags = List.of("tag1", "tag2");

            assertThatThrownBy(() -> linkService.updateLink(id, titleAfterUpdate, linkUrlAfterUpdate, newTags))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("요청한 자원을 찾을 수 없습니다");
        }


        @DisplayName("링크 수정 시 입력값으로 받은 태그로 링크의 태그를 변경한다")
        @Test
        void update_link_when_tags_changed() {
            // given
            String titleBeforeUpdate = "변경 전 타이틀";
            String linkUrlBeforeUpdate = "https://www.naver.com";

            var linkToSave = Link.create(titleBeforeUpdate, linkUrlBeforeUpdate);
            var savedLink = linkRepository.save(linkToSave);

            String tag1 = "tag1";
            String tag2 = "tag2";
            var savedTag1 = tagRepository.save(Tag.create(tag1));
            var savedTag2 = tagRepository.save(Tag.create(tag2));

            tagLinkMapRepository.save(TagLinkMap.create(savedLink, savedTag1));
            tagLinkMapRepository.save(TagLinkMap.create(savedLink, savedTag2));


            // when
            String titleAfterUpdate = "변경 후 타이틀";
            String linkUrlAfterUpdate = "https://www.google.com";
            List<String> newTags = List.of("tag2", "tag3");
            linkService.updateLink(savedLink.getId(), titleAfterUpdate, linkUrlAfterUpdate, newTags);
            var result = linkQueryDslRepository.findLinkByIdWithTags(savedLink.getId());

            // then
            assertThat(result.get().getTags()).extracting("name").containsExactlyInAnyOrder("tag2", "tag3");
        }
    }
}
