package com.yourink.domain.tag;


import com.yourink.domain.base.BaseAuditEntity;
import com.yourink.domain.link.Link;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TagLinkMap extends BaseAuditEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "link_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Link link;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Tag tag;

    private TagLinkMap(Link link, Tag tag) {
        this.link = link;
        this.tag = tag;
    }

    public static TagLinkMap create(Link link, Tag tag) {
        return new TagLinkMap(link, tag);
    }
}
