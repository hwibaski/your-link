package com.yourink.linkStat.service;

import com.yourink.linkStat.controller.dto.GetLinkCountResponse;
import com.yourink.repository.linkStat.LinkStatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LinkStatReadService {
    private final LinkStatRepository linkStatRepository;

    @Transactional(readOnly = true)
    public GetLinkCountResponse countAll() {
        return new GetLinkCountResponse(linkStatRepository.count());
    }
}
