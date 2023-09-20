package com.yourink.tag.service;

import com.yourink.domain.link.Link;
import com.yourink.domain.tag.Tag;
import com.yourink.domain.tag.TagLinkMap;
import com.yourink.repository.tag.TagLinkMapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagLinkMapService {
    private final TagLinkMapRepository tagLinkMapRepository;
    private final TagService tagService;

    @Transactional
    public List<TagLinkMap> createTagLinkMap(Link link, List<String> tagNames) {
        List<Tag> tags = tagService.createTags(tagNames);
        List<TagLinkMap> tagLinkMaps = mappingTagsAndLink(link, tags);

        return tagLinkMapRepository.saveAll(tagLinkMaps);
    }

    private List<TagLinkMap> mappingTagsAndLink(Link link, List<Tag> tags) {
        return tags.stream()
                   .map(tag -> TagLinkMap.create(link, tag))
                   .toList();
    }
}
