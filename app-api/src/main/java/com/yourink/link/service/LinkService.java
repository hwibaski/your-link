package com.yourink.link.service;

import com.yourink.domain.link.Link;
import com.yourink.dto.api.ErrorCode;
import com.yourink.dto.link.LinkResponse;
import com.yourink.dto.page.CursorResult;
import com.yourink.exception.NotFoundException;
import com.yourink.repository.link.LinkQueryDslRepository;
import com.yourink.repository.link.LinkRepository;
import com.yourink.service.PaginationService;
import com.yourink.tag.service.TagLinkMapService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LinkService {
    private final LinkRepository linkRepository;
    private final LinkQueryDslRepository linkQueryDslRepository;
    private final PaginationService paginationService;
    private final TagLinkMapService tagLinkMapService;

    public LinkService(LinkRepository linkRepository,
            LinkQueryDslRepository linkQueryDslRepository, TagLinkMapService tagLinkMapService) {
        this.linkRepository = linkRepository;
        this.linkQueryDslRepository = linkQueryDslRepository;
        this.paginationService = new PaginationService(linkRepository);
        this.tagLinkMapService = tagLinkMapService;
    }

    @Transactional
    public LinkResponse createLink(String title, String linkUrl, List<String> tags) {
        var savedLink = linkRepository.save(Link.create(title, linkUrl));
        tagLinkMapService.createTagLinkMap(savedLink, tags);

        return new LinkResponse(savedLink.getId(), savedLink.getTitle(), savedLink.getLinkUrl());
    }

    @Transactional
    public LinkResponse updateLink(Long id, String title, String linkUrl) {
        var link = findLinkById(id);

        link.update(title, linkUrl);

        return new LinkResponse(link.getId(), link.getTitle(), link.getLinkUrl());
    }

    @Transactional(readOnly = true)
    public CursorResult<LinkResponse> getALlLinksByIdDesc(Long linkId, Integer pageSize) {
        var links = linkQueryDslRepository.findAllLinksByIdLessThanDesc(linkId, pageSize);

        return new CursorResult<>(links.stream()
                                       .map(link -> new LinkResponse(link.getId(), link.getTitle(), link.getLinkUrl()))
                                       .collect(Collectors.toList()), paginationService.hasNext(links));
    }

    @Transactional(readOnly = true)
    public LinkResponse getLink(Long linkId) {
        var link = findLinkById(linkId);

        return new LinkResponse(link.getId(), link.getTitle(), link.getLinkUrl());
    }

    private Link findLinkById(Long linkId) {
        return linkRepository.findById(linkId)
                             .orElseThrow(
                                     () -> new NotFoundException(ErrorCode.NOT_FOUND.getMessage(), ErrorCode.NOT_FOUND.getCode(), ErrorCode.NOT_FOUND.getStatus())
                             );
    }
}
