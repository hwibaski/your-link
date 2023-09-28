package com.yourink.taglinkmap.filter;

import com.yourink.domain.tag.Tag;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CreateNewTagClassifier {
    public List<String> filterNewTagsToCreate(List<Tag> existsTag, List<String> newTags) {
        return newTags.stream()
                      .filter(tag -> existsTag.stream()
                                              .noneMatch(linkTag -> linkTag.getName().equals(tag)))
                      .toList();
    }
}
