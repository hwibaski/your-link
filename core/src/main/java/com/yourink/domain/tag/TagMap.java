package com.yourink.domain.tag;


import com.yourink.domain.base.BaseAuditEntity;
import com.yourink.domain.link.Link;
import com.yourink.domain.member.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TagMap extends BaseAuditEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "link_id")
    private Link link;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    private TagMap(Member member, Link link, Tag tag) {
        this.member = member;
        this.link = link;
        this.tag = tag;
    }

    public TagMap create(Member member, Link link, Tag tag) {
        return new TagMap(member, link, tag);
    }
}
