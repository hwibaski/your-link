package com.yourink.tag.service;

import com.yourink.domain.link.Link;
import com.yourink.domain.tag.Tag;
import com.yourink.domain.tag.TagLinkMap;
import com.yourink.repository.tag.TagLinkMapQueryDslRepository;
import com.yourink.repository.tag.TagLinkMapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagLinkMapWriteService {
    private final TagLinkMapRepository tagLinkMapRepository;
    private final TagWriteService tagWriteService;
    private final TagLinkMapQueryDslRepository tagLinkMapQueryDslRepository;

    @Transactional
    public List<TagLinkMap> createTagLinkMap(Link link, List<String> tagNames) {
        List<Tag> tags = tagWriteService.createTags(tagNames);
        List<TagLinkMap> tagLinkMaps = mappingTagsAndLink(link, tags);

        return tagLinkMapRepository.saveAll(tagLinkMaps);
    }

    private List<TagLinkMap> mappingTagsAndLink(Link link, List<Tag> tags) {
        return tags.stream()
                   .map(tag -> TagLinkMap.create(link, tag))
                   .toList();
    }

    @Transactional
    public void replaceTagLinkMap(Link link, List<String> newTags) {
        var tagsToDelete = filterTagsToDelete(link, newTags);
        var tagsToCreate = filterNewTagsToCreate(link, newTags);
        createTagLinkMap(link, tagsToCreate);
        deleteTagLinkMapByLinkAndTags(link, tagsToDelete);
    }

    private void deleteTagLinkMapByLinkAndTags(Link link, List<Tag> tags) {
        tagLinkMapQueryDslRepository.deleteByLinkInAndTag(link, tags);
    }

    private List<String> filterNewTagsToCreate(Link link, List<String> newTags) {
        return newTags.stream()
                      .filter(tag -> link.getTags().stream()
                                         .noneMatch(linkTag -> linkTag.getName().equals(tag)))
                      .toList();
    }

    private List<Tag> filterTagsToDelete(Link link, List<String> newTags) {
        return link.getTags()
                   .stream()
                   .filter(tag -> !newTags.contains(tag.getName())).toList();
    }
}
