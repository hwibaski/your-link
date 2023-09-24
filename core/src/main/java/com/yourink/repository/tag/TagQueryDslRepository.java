package com.yourink.repository.tag;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yourink.domain.tag.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.yourink.domain.tag.QTag.tag;
import static com.yourink.domain.tag.QTagLinkMap.tagLinkMap;

@Repository
@RequiredArgsConstructor
public class TagQueryDslRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public List<Tag> getTagsByLinkIdOrderByIdAsc(Long linkId) {
        return jpaQueryFactory.selectFrom(tag)
                              .join(tagLinkMap).on(tagLinkMap.tag.eq(tag))
                              .where(tagLinkMap.link.id.eq(linkId))
                              .orderBy(tag.id.asc())
                              .fetch();
    }
}
