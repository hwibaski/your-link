package com.yourink.repository.link;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yourink.domain.link.Link;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.yourink.domain.link.QLink.link;
import static com.yourink.domain.tag.QTag.tag;
import static com.yourink.domain.tag.QTagLinkMap.tagLinkMap;

@Repository
@RequiredArgsConstructor
public class LinkQueryDslRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public List<Link> findAllLinksByIdLessThanDesc(Long linkId, Integer pageSize) {
        BooleanBuilder dynamicLtId = new BooleanBuilder();

        if (linkId != null) {
            dynamicLtId.and(link.id.lt(linkId));
        }

        return jpaQueryFactory.selectFrom(link)
                              .where(dynamicLtId)
                              .orderBy(link.id.desc())
                              .limit(pageSize)
                              .fetch();
    }

    public Optional<Link> findLinkByIdWithTags(Long linkId) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(link)
                                                  .leftJoin(link.tagLinkMaps, tagLinkMap)
                                                  .fetchJoin()
                                                  .leftJoin(tagLinkMap.tag, tag)
                                                  .fetchJoin()
                                                  .where(link.id.eq(linkId))
                                                  .fetchOne());
    }
}
