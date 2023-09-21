package com.yourink.repository.tag;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yourink.domain.link.Link;
import com.yourink.domain.tag.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.yourink.domain.tag.QTagLinkMap.tagLinkMap;

@Repository
@RequiredArgsConstructor
public class TagLinkMapQueryDslRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public void deleteByLinkInAndTag(Link link, List<Tag> tags) {
        jpaQueryFactory.delete(tagLinkMap)
                       .where(tagLinkMap.link.in(link)
                                             .and(tagLinkMap.tag.in(tags)))
                       .execute();
    }
}
