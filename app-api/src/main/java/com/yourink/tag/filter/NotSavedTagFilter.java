package com.yourink.tag.filter;

import com.yourink.domain.tag.Tag;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class NotSavedTagFilter {
    public List<String> filterNotSavedTag(List<Tag> existsTags, List<String> filteredValidTags) {
        var existsTagNameSet = getExistsTagNames(existsTags);

        return filteredValidTags.stream()
                                .filter(tag -> !existsTagNameSet.contains(tag))
                                .toList();
    }

    private Set<String> getExistsTagNames(List<Tag> validTags) {
        return validTags.stream()
                        .map(Tag::getName)
                        .collect(Collectors.toSet());
    }
}
