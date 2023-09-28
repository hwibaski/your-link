package com.yourink.taglinkmap.service;

import com.yourink.domain.link.Link;
import com.yourink.domain.tag.Tag;
import com.yourink.domain.tag.TagLinkMap;
import com.yourink.repository.tag.TagLinkMapQueryDslRepository;
import com.yourink.repository.tag.TagLinkMapRepository;
import com.yourink.tag.service.TagWriteService;
import com.yourink.taglinkmap.filter.CreateNewTagClassifier;
import com.yourink.taglinkmap.filter.DeleteTagClassifier;
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
    private final CreateNewTagClassifier createNewTagClassifier;
    private final DeleteTagClassifier deleteTagClassifier;

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
        var tagsToDelete = deleteTagClassifier.filterTagsToDelete(link.getTags(), newTags);
        var tagsToCreate = createNewTagClassifier.filterNewTagsToCreate(link.getTags(), newTags);
        createTagLinkMap(link, tagsToCreate);
        deleteTagLinkMapByLinkAndTags(link, tagsToDelete);
    }

    private void deleteTagLinkMapByLinkAndTags(Link link, List<Tag> tags) {
        tagLinkMapQueryDslRepository.deleteByLinkInAndTag(link, tags);
    }
}
