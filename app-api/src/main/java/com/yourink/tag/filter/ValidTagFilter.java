package com.yourink.tag.filter;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ValidTagFilter {
    private static final int TAG_NAME_MAX_LENGTH = 20;
    private static final int FIRST_INDEX = 0;

    public List<String> filterValidTags(List<String> tags) {
        return tags.stream()
                   .filter(this::isValidTag)
                   .distinct()
                   .collect(Collectors.toList());
    }

    private boolean isValidTag(String tag) {
        return isLengthLessEqualTwenty(tag) &&
                isFirstCharIsLetter(tag) &&
                isConsistOfOnlyLetterOrDigit(tag);
    }

    private boolean isLengthLessEqualTwenty(String tag) {
        return tag.length() <= TAG_NAME_MAX_LENGTH;
    }

    private boolean isFirstCharIsLetter(String tag) {
        return Character.isLetter(tag.charAt(FIRST_INDEX));
    }

    private boolean isConsistOfOnlyLetterOrDigit(String tag) {
        return tag.chars().allMatch(Character::isLetterOrDigit);
    }
}
