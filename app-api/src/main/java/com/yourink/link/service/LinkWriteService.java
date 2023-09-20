package com.yourink.link.service;

import com.yourink.domain.link.Link;
import com.yourink.repository.link.LinkRepository;
import com.yourink.tag.service.TagLinkMapService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LinkWriteService {
    private final LinkRepository linkRepository;
    private final TagLinkMapService tagLinkMapService;
    private final LinkReadService linkReadService;

    @Transactional
    public Link createLink(String title, String linkUrl, List<String> tags) {
        var savedLink = linkRepository.save(Link.create(title, linkUrl));
        tagLinkMapService.createTagLinkMap(savedLink, tags);

        return savedLink;
    }

    @Transactional
    public Link updateLink(Long id, String title, String linkUrl) {
        var link = linkReadService.findLinkById(id);

        link.update(title, linkUrl);

        return link;
    }
}
