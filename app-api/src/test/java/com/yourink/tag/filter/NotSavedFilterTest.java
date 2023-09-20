package com.yourink.tag.filter;

import com.yourink.domain.tag.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class NotSavedFilterTest {
    @Autowired
    private NotSavedTagFilter notSavedTagFilter;

    @Test
    @DisplayName("저장된 태그 리스트와  저장할 태그 이름 리스트가 주어지면 저장되지 않은 태그 이름 리스트를 반환한다.")
    void filter_not_saved_tags() {
        // given
        List<Tag> savedTags = List.of(Tag.create("tag1"), Tag.create("tag2"));
        List<String> tags = List.of("tag1", "tag2", "tag3");

        // when
        List<String> result = notSavedTagFilter.filterNotSavedTag(savedTags, tags);

        // then
        assertThat(result).containsExactly("tag3");
    }

    @Test
    @DisplayName("저장할 태그가 이미 모두 저장되어 있다면 빈 리스트를 반환한다.")
    void return_empty_list_if_all_tags_are_already_saved() {
        // given
        List<Tag> savedTags = List.of(Tag.create("tag1"), Tag.create("tag2"), Tag.create("tag3"));
        List<String> tags = List.of("tag1", "tag2", "tag3");

        // when
        List<String> result = notSavedTagFilter.filterNotSavedTag(savedTags, tags);

        // then
        assertThat(result).isEmpty();
    }

}
