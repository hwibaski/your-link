package com.yourink.link.service;

import com.yourink.domain.link.Link;
import com.yourink.dto.api.ErrorCode;
import com.yourink.dto.link.LinkResponse;
import com.yourink.dto.page.CursorResult;
import com.yourink.exception.NotFoundException;
import com.yourink.repository.link.LinkQueryDslRepository;
import com.yourink.repository.link.LinkRepository;
import com.yourink.service.PaginationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class LinkReadService {
    private final LinkRepository linkRepository;
    private final LinkQueryDslRepository linkQueryDslRepository;
    private final PaginationService paginationService;

    public LinkReadService(LinkRepository linkRepository,
            LinkQueryDslRepository linkQueryDslRepository) {
        this.linkRepository = linkRepository;
        this.linkQueryDslRepository = linkQueryDslRepository;
        this.paginationService = new PaginationService(linkRepository);
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

    protected Link findLinkById(Long linkId) {
        return linkRepository.findById(linkId)
                             .orElseThrow(
                                     () -> new NotFoundException(ErrorCode.NOT_FOUND.getMessage(), ErrorCode.NOT_FOUND.getCode(), ErrorCode.NOT_FOUND.getStatus())
                             );
    }
}
