package com.yourink.tag.filter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class ValidTagFilterTest {
    @Autowired
    private ValidTagFilter validTagFilter;

    @Test
    @DisplayName("문자로 시작하는 태그만 필터링한다.")
    void tag_name_should_be_started_with_string() {
        // given
        String tagStartsWithDigit = "1Tag";
        String tagStartsWithChar = "Tag";
        List<String> tags = List.of(tagStartsWithDigit, tagStartsWithChar);

        // when
        List<String> result = validTagFilter.filterValidTags(tags);

        // then
        assertThat(result).containsExactly(tagStartsWithChar);
    }

    @Test
    @DisplayName("태그 이름에 공백은 허용되지 않는다.")
    void tag_name_should_not_contain_white_space() {
        // given
        String tagWithWhiteSpace = "T ag";
        String validTag = "Tag";
        List<String> tags = List.of(tagWithWhiteSpace, validTag);

        // when
        List<String> result = validTagFilter.filterValidTags(tags);

        // then
        assertThat(result).containsExactly(validTag);
    }

    @Test
    @DisplayName("태그 이름은 영문자와 숫자만 가능합니다")
    void tag_name_should_be_consist_of_char_and_digit() {
        // given
        String tagWithSpecialChar = "T@g";
        String validTag = "Tag";
        List<String> tags = List.of(tagWithSpecialChar, validTag);

        // when
        List<String> result = validTagFilter.filterValidTags(tags);

        // then
        assertThat(result).containsExactly(validTag);
    }

    @Test
    @DisplayName("태그 이름은 20자를 넘을 수 없습니다. (20자까지 허용 - 현재 테스트 문자열 길이 : 20자)")
    void tag_name_length_should_be_shorter_than_20() {
        // given
        String tagHas20Length = "ab123456789123456789";
        List<String> tags = List.of(tagHas20Length);

        // when
        List<String> result = validTagFilter.filterValidTags(tags);

        // then
        assertThat(result).containsExactly(tagHas20Length);
    }

    @Test
    @DisplayName("태그 이름은 20자를 넘을 수 없습니다. (20자까지 허용 - 현재 테스트 문자열 길이 : 21자)")
    void tag_name_length_should_not_be_21() {
        // given
        String tagHas21Length = "ab1234567891234567891";
        List<String> tags = List.of(tagHas21Length);

        // when
        List<String> result = validTagFilter.filterValidTags(tags);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("태그 이름은 20자를 넘을 수 없습니다. (20자까지 허용 - 현재 테스트 문자열 길이 : 19자)")
    void tag_name_length_valid_when_19_length() {
        // given
        String tagHas19Length = "ab12345678912345678";
        List<String> tags = List.of(tagHas19Length);

        // when
        List<String> result = validTagFilter.filterValidTags(tags);

        // then
        assertThat(result).containsExactly(tagHas19Length);
    }
}
