package com.yourink.tag.service;

import com.yourink.domain.tag.Tag;
import com.yourink.repository.tag.TagQueryDslRepository;
import com.yourink.tag.controller.dto.GetTagListByLinkResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagReadService {
    private final TagQueryDslRepository tagQueryDslRepository;

    @Transactional(readOnly = true)
    public GetTagListByLinkResponse getTagsByLink(Long linkId) {
        List<Tag> tags = tagQueryDslRepository.getTagsByLinkIdOrderByIdAsc(linkId);

        return new GetTagListByLinkResponse(tags.stream()
                                                .map(Tag::getName)
                                                .toList());
    }
}
