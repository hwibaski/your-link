package com.yourink.taglinkmap.filter;

import com.yourink.domain.tag.Tag;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeleteTagClassifier {
    public List<Tag> filterTagsToDelete(List<Tag> existsTags, List<String> newTags) {
        return existsTags
                .stream()
                .filter(tag -> !newTags.contains(tag.getName())).toList();
    }
}
