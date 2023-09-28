package com.yourink.taglinkmap.service;

import com.yourink.domain.link.Link;
import com.yourink.domain.member.Member;
import com.yourink.domain.tag.Tag;
import com.yourink.domain.tag.TagLinkMap;
import com.yourink.repository.link.LinkQueryDslRepository;
import com.yourink.repository.link.LinkRepository;
import com.yourink.repository.member.MemberRepository;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class TagLinkMapWriteServiceTest {
    @Autowired
    private TagLinkMapWriteService tagLinkMapWriteService;

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TagLinkMapRepository tagLinkMapRepository;

    @Autowired
    private LinkQueryDslRepository linkQueryDslRepository;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
        tagRepository.deleteAllInBatch();
        linkRepository.deleteAllInBatch();
        tagLinkMapRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }


    @Nested
    @DisplayName("TagLinkMap 생성 테스트")
    class TagLinkMapCreateTest {
        @Test
        @DisplayName("Link와 Tag의 연결 객체인 TagLinkMap을 생성한다")
        void create_tag_link_map() {
            // given
            Member savedMember = memberRepository.save(Member.create("test@gmail.com"));
            var link = Link.create("title", "linkUrl", savedMember);
            var tag = Tag.create("tag1");

            var savedLink = linkRepository.save(link);
            var savedTag = tagRepository.save(tag);

            // when
            var result = tagLinkMapWriteService.createTagLinkMap(savedLink, List.of(savedTag.getName()));

            // then
            assertThat(result)
                    .extracting(TagLinkMap::getLink)
                    .containsExactly(savedLink);

            assertThat(result)
                    .extracting(TagLinkMap::getTag)
                    .containsExactly(savedTag);
        }
    }

    @Nested
    @DisplayName("링크의 TagLinkMap 교체 테스트")
    class TagLinkMapReplaceTest {

        @Test
        @DisplayName("주어진 문자열 태그 리스트와 기존의 링크가 가지고 있는 태그들의 이름을 대조한 뒤 겹치지 않는 태그는 새로 생성하고, 주어진 문자열과 겹치지 않는 태그의 TagLinkMap을 삭제한다")
        void replace_tag_link_map() {
            // given
            Member savedMember = memberRepository.save(Member.create("test@gmail.com"));
            var link = Link.create("title", "linkUrl", savedMember);
            var tag = Tag.create("tag1");

            var savedLink = linkRepository.save(link);
            var savedTag = tagRepository.save(tag);

            tagLinkMapRepository.save(TagLinkMap.create(savedLink, savedTag));

            // when
            tagLinkMapWriteService.replaceTagLinkMap(savedLink, List.of("tag2", "tag3"));
            Optional<Link> result = linkQueryDslRepository.findLinkByIAndMemberIdWithTags(savedLink.getId(), savedMember.getId());

            // then
            assertThat(result.get().getTags()).extracting("name").containsExactly("tag2", "tag3");
        }
    }
}
