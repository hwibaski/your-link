package com.yourink.tag.sanitizer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("test")
@SpringBootTest
class TagSanitizerTest {
    @Autowired
    private TagSanitizer tagSanitizer;

    @Test
    @DisplayName("태그를 소문자로 변경한다")
    void to_lower_case_tag() {
        // given
        String tag1 = "Abc";
        String tag2 = "DEF";
        List<String> tags = List.of(tag1, tag2);

        // when
        List<String> result = tagSanitizer.trimAndToLowerCase(tags);

        // then
        assertThat(result).containsExactly("abc", "def");
    }

    @Test
    @DisplayName("태그들의 앞뒤 공백을 제거한다.")
    void trim_tag() {
        // given
        String tag1 = "  abc   ";
        String tag2 = "    def    ";
        List<String> tags = List.of(tag1, tag2);

        // when
        List<String> result = tagSanitizer.trimAndToLowerCase(tags);

        // then
        assertThat(result).containsExactly("abc", "def");
    }
}
