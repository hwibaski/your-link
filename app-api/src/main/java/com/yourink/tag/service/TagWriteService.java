package com.yourink.tag.service;

import com.yourink.domain.tag.Tag;
import com.yourink.repository.tag.TagRepository;
import com.yourink.tag.filter.NotSavedTagFilter;
import com.yourink.tag.filter.ValidTagFilter;
import com.yourink.tag.sanitizer.TagSanitizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TagWriteService {
    private final NotSavedTagFilter notSavedTagFilter;
    private final TagRepository tagRepository;
    private final ValidTagFilter validTagFilter;
    private final TagSanitizer tagSanitizer;

    @Transactional
    public List<Tag> createTags(List<String> tags) {
        var filteredValidTags = getValidTags(tags);

        var existingTags = tagRepository.findByNameIsIn(filteredValidTags);
        var tagsToSave = notSavedTagFilter.filterNotSavedTag(existingTags, filteredValidTags);

        if (tagsToSave.isEmpty()) {
            return existingTags;
        }

        var savedTags = tagRepository.saveAll(tagsToSave.stream().map(Tag::create).toList());
        var allTags = Stream.concat(existingTags.stream(), savedTags.stream()).collect(Collectors.toList());

        return allTags;
    }

    private List<String> getValidTags(List<String> tags) {
        var sanitizedTags = tagSanitizer.trimAndToLowerCase(tags);
        return validTagFilter.filterValidTags(sanitizedTags);
    }
}
