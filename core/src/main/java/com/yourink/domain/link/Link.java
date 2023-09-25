package com.yourink.domain.link;

import com.yourink.domain.base.BaseAuditEntity;
import com.yourink.domain.member.Member;
import com.yourink.domain.tag.Tag;
import com.yourink.domain.tag.TagLinkMap;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Link extends BaseAuditEntity {
    private String title;

    private String linkUrl;

    private int hitCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "link", fetch = FetchType.LAZY)
    private List<TagLinkMap> tagLinkMaps = new ArrayList<>();

    private Link(String title, String linkUrl, Member member) {
        this.title = title;
        this.linkUrl = linkUrl;
        this.member = member;
    }

    public static Link create(String title, String linkUrl, Member member) {
        return new Link(title, linkUrl, member);
    }

    public void update(String title, String linkUrl) {
        this.title = title;
        this.linkUrl = linkUrl;
    }

    public void addTagLinkMap(TagLinkMap tagLinkMap) {
        tagLinkMaps.add(tagLinkMap);
    }

    public List<Tag> getTags() {
        return tagLinkMaps.stream()
                          .map(TagLinkMap::getTag)
                          .toList();
    }
}
