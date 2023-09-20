package com.yourink.tag.service;

import com.yourink.domain.link.Link;
import com.yourink.domain.tag.Tag;
import com.yourink.domain.tag.TagLinkMap;
import com.yourink.repository.link.LinkRepository;
import com.yourink.repository.tag.TagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class TagLinkMapServiceTest {
    @Autowired
    private TagLinkMapService tagLinkMapService;

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private TagRepository tagRepository;

    @Test
    @DisplayName("Link와 Tag의 연결 객체인 TagLinkMap을 생성한다")
    void create_tag_link_map() {
        // given
        var link = Link.create("title", "linkUrl");
        var tag = Tag.create("tag1");

        var savedLink = linkRepository.save(link);
        var savedTag = tagRepository.save(tag);

        // when
        var result = tagLinkMapService.createTagLinkMap(savedLink, List.of(savedTag.getName()));

        // then
        assertThat(result)
                .extracting(TagLinkMap::getLink)
                .containsExactly(savedLink);

        assertThat(result)
                .extracting(TagLinkMap::getTag)
                .containsExactly(savedTag);
    }
}
