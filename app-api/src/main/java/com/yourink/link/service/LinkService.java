package com.yourink.link.service;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yourink.domain.link.Link;
import com.yourink.dto.api.ErrorCode;
import com.yourink.dto.link.LinkResponse;
import com.yourink.dto.page.CursorResult;
import com.yourink.exception.NotFoundException;
import com.yourink.repository.link.LinkQueryDslRepository;
import com.yourink.repository.link.LinkRepository;
import com.yourink.service.PaginationService;

@Service
public class LinkService {
    private final LinkRepository linkRepository;
    private final LinkQueryDslRepository linkQueryDslRepository;
    private final PaginationService paginationService;

    public LinkService(LinkRepository linkRepository,
                       LinkQueryDslRepository linkQueryDslRepository) {
        this.linkRepository = linkRepository;
        this.linkQueryDslRepository = linkQueryDslRepository;
        this.paginationService = new PaginationService(linkRepository);
    }

    @Transactional
    public LinkResponse createLink(String title, String linkUrl) {
        var savedLink = linkRepository.save(Link.create(title, linkUrl));

        return new LinkResponse(savedLink.getId(), savedLink.getTitle(), savedLink.getLinkUrl());
    }

    @Transactional
    public LinkResponse updateLink(Long id, String title, String linkUrl) {
        var link = linkRepository.findById(id)
                                 .orElseThrow(
                                         () -> new NotFoundException(ErrorCode.NOT_FOUND.getMessage(), ErrorCode.NOT_FOUND.getCode(), ErrorCode.NOT_FOUND.getStatus())
                                 );

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
}
