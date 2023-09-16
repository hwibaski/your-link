package com.yourink.link.service;

import com.yourink.domain.link.Link;
import com.yourink.dto.link.LinkResponse;
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
}
