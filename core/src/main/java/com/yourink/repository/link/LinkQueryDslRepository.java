package com.yourink.repository.link;


import static com.yourink.domain.link.QLink.link;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yourink.domain.link.Link;

import lombok.RequiredArgsConstructor;

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
}
