package com.yourink.taglinkmap.filter;

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
class DeleteTagClassifierTest {
    @Autowired
    private DeleteTagClassifier deleteTagClassifier;

    @Test
    @DisplayName("주어진 태그 리스트와 새로운 태그 이름 문자열 리스트를 비교한 후 새로운 태그 리스트에 포함되지 않은 태그 리스트를 반환한다.")
    void filter_tags_to_delete() {
        // given
        var existTag1 = Tag.create("tag1");
        var existTag2 = Tag.create("tag2");

        var newTag1 = "tag2";
        var newTag2 = "tag3";

        // when
        var result = deleteTagClassifier.filterTagsToDelete(List.of(existTag1, existTag2), List.of(newTag1, newTag2));

        // then
        assertThat(result).extracting(Tag::getName).containsExactly(existTag1.getName());
    }

    @Test
    @DisplayName("주어진 태그 리스트와 새로운 태그 이름 문자열 리스트의 태그 이름이 모두 같다면 비어있는 리스트를 반환한다.")
    void filter_tags_to_delete_when_no_tags_to_delete() {
        // given
        var existTag1 = Tag.create("tag1");
        var existTag2 = Tag.create("tag2");

        var newTag1 = "tag1";
        var newTag2 = "tag2";

        // when
        var result = deleteTagClassifier.filterTagsToDelete(List.of(existTag1, existTag2), List.of(newTag1, newTag2));

        // then
        assertThat(result).isEmpty();
    }
}
