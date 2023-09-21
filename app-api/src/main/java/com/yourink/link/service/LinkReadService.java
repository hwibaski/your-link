package com.yourink.link.service;

import com.yourink.domain.link.Link;
import com.yourink.dto.api.ErrorCode;
import com.yourink.dto.page.CursorResult;
import com.yourink.exception.NotFoundException;
import com.yourink.link.controller.dto.GetLinkListResponse;
import com.yourink.link.controller.dto.GetLinkResponse;
import com.yourink.repository.link.LinkQueryDslRepository;
import com.yourink.repository.link.LinkRepository;
import com.yourink.service.PaginationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LinkReadService {
    private final LinkQueryDslRepository linkQueryDslRepository;
    private final PaginationService paginationService;

    public LinkReadService(LinkRepository linkRepository, LinkQueryDslRepository linkQueryDslRepository) {
        this.linkQueryDslRepository = linkQueryDslRepository;
        this.paginationService = new PaginationService(linkRepository);
    }

    @Transactional(readOnly = true)
    public CursorResult<GetLinkListResponse> getALlLinksByIdDesc(Long linkId, Integer pageSize) {
        var links = linkQueryDslRepository.findAllLinksByIdLessThanDesc(linkId, pageSize);


        return new CursorResult<>(mapLinkListToResponse(links), paginationService.hasNext(links));
    }

    private List<GetLinkListResponse> mapLinkListToResponse(List<Link> links) {
        return links.stream()
                    .map(this::mapLinkToResponse)
                    .toList();
    }


    private GetLinkListResponse mapLinkToResponse(Link link) {
        var tags = mappingTagsFromLink(link);

        return new GetLinkListResponse(link.getId(), link.getTitle(), link.getLinkUrl(), tags);
    }

    @Transactional(readOnly = true)
    public GetLinkResponse getLink(Long linkId) {
        var link = findLinkByIdWithTag(linkId);


        return new GetLinkResponse(link.getId(), link.getTitle(), link.getLinkUrl(), mappingTagsFromLink(link));
    }

    protected Link findLinkByIdWithTag(Long linkId) {
        return linkQueryDslRepository.findLinkByIdWithTags(linkId)
                                     .orElseThrow(
                                             () -> new NotFoundException(ErrorCode.NOT_FOUND.getMessage(), ErrorCode.NOT_FOUND.getCode(), ErrorCode.NOT_FOUND.getStatus())
                                     );
    }

    private List<String> mappingTagsFromLink(Link link) {
        return link.getTagLinkMaps()
                   .stream()
                   .map(tagLinkMap -> tagLinkMap.getTag().getName())
                   .toList();
    }
}
