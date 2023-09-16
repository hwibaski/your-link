package com.yourink.link.service;

import com.yourink.domain.link.Link;
import com.yourink.dto.api.ErrorCode;
import com.yourink.dto.link.LinkResponse;
import com.yourink.exception.NotFoundException;
import com.yourink.repository.link.LinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LinkService {
    private final LinkRepository linkRepository;

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
}
