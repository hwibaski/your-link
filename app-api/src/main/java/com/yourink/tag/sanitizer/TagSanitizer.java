package com.yourink.tag.sanitizer;

import org.springframework.stereotype.Component;

import java.util.List;

// TODO: 테스트 코드 작성
@Component
public class TagSanitizer {
    public List<String> trimAndToLowerCase(List<String> tags) {
        return toLowerCaseList(trimEachString(tags));
    }

    private List<String> trimEachString(List<String> tags) {
        return tags.stream()
                   .map(String::trim)
                   .toList();
    }

    private List<String> toLowerCaseList(List<String> tags) {
        return tags.stream()
                   .map(String::toLowerCase)
                   .toList();
    }
}
