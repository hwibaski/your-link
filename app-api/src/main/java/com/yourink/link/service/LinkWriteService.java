package com.yourink.link.service;

import com.yourink.domain.link.Link;
import com.yourink.domain.member.Member;
import com.yourink.repository.link.LinkRepository;
import com.yourink.taglinkmap.service.TagLinkMapWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LinkWriteService {
    private final LinkRepository linkRepository;
    private final TagLinkMapWriteService tagLinkMapWriteService;
    private final LinkReadService linkReadService;

    @Transactional
    public Link createLink(String title, String linkUrl, List<String> tags, Member member) {
        var savedLink = linkRepository.save(Link.create(title, linkUrl, member));
        tagLinkMapWriteService.createTagLinkMap(savedLink, tags);

        return savedLink;
    }

    @Transactional
    public Link updateLink(Long id, String title, String linkUrl, List<String> newTags) {
        var link = linkReadService.findLinkByIdWithTag(id);
        tagLinkMapWriteService.replaceTagLinkMap(link, newTags);
        link.update(title, linkUrl);

        return link;
    }
}
