package com.yourink.tag.service;

import com.yourink.domain.tag.Tag;
import com.yourink.repository.tag.TagRepository;
import com.yourink.tag.filter.NotSavedTagFilter;
import com.yourink.tag.filter.ValidTagFilter;
import com.yourink.tag.sanitizer.TagSanitizer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@SpringBootTest
class TagWriteServiceTest {
    @Autowired
    private TagWriteService tagWriteService;

    @Autowired
    private TagRepository tagRepository;

    @SpyBean
    private NotSavedTagFilter notSavedTagFilter;


    @SpyBean
    private ValidTagFilter validTagFilter;

    @SpyBean
    private TagSanitizer tagSanitizer;


    @AfterEach
    void tearDown() {
        tagRepository.deleteAllInBatch();
    }

    @Nested()
    @DisplayName("태그 생성 테스트")
    class CreateTagTest {
        @Test
        @DisplayName("태그를 생성한다")
        void createTag_success() {
            // given
            String tag1 = "tag1";
            String tag2 = "tag2";
            String tag3 = "tag3";
            List<String> tags = List.of(tag1, tag2, tag3);

            // when
            var result = tagWriteService.createTags(tags);

            // then
            assertThat(result).extracting("name").containsExactly(tag1, tag2, tag3);
        }

        @Test
        @DisplayName("이미 존재하는 태그가 있을 경우 새로운 태그를 만들지 않고 기존에 존재하는 태그를 생성하기 위해 TagFilterService.getTagsToSave 메서드가 호출된다.")
        void createTag_calls_tagFilterService() {
            // given
            String tag1 = "tag1";
            List<String> tagsToSave = List.of(tag1);

            // when
            tagWriteService.createTags(tagsToSave);

            // then
            verify(notSavedTagFilter, times(1)).filterNotSavedTag(any(), any());
        }

        @Test
        @DisplayName("태그 이름에 포함된 영어 대문자는 모두 소문자로 변환된다.")
        void createTag_every_english_char_will_convert_to_lowercase() {
            // given
            String tag1 = "Tag1";
            List<String> tags = List.of(tag1);

            // when
            var result = tagWriteService.createTags(tags);

            // then
            assertThat(result).extracting("name").containsExactly("tag1");
        }

        @Test
        @DisplayName("태그 생성 시 tagValidator.filterValidTags() 메서드가 호출된다.")
        void createTag_will_call_tagValidator_validatorTagName() {
            // given
            String tag1 = "tag1";
            List<String> tags = List.of(tag1);

            // when
            tagWriteService.createTags(tags);

            // then
            verify(validTagFilter, times(1)).filterValidTags(any());
        }

        @Test
        @DisplayName("태그 생성 시 tagSanitizer.trimAndToLowerCase() 메서드가 호출된다.")
        void createTag_will_call_tagValidator_validatorTagName2() {
            // given
            String tag1 = "tag1";
            List<String> tags = List.of(tag1);

            // when
            tagWriteService.createTags(tags);

            // then
            verify(tagSanitizer, times(1)).trimAndToLowerCase(any());
        }

        @Test
        @DisplayName("20자가 넘는 tag는 생성되지 않는다.")
        void createTag_tag_length_is_shorter_than_20() {
            // given
            String tag1 = "abcdefghijklmnopqrstu";
            String tag2 = "abcd";
            List<String> tags = List.of(tag1, tag2);

            // when
            List<Tag> result = tagWriteService.createTags(tags);

            // then
            assertThat(result).extracting("name").containsExactly(tag2);
        }
    }
}
