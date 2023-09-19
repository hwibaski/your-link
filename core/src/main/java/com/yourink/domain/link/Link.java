package com.yourink.domain.link;

import com.yourink.domain.base.BaseAuditEntity;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Link extends BaseAuditEntity {
    private String title;

    private String linkUrl;

    // TODO: 생성자 private으로 변경
    public Link(String title, String linkUrl) {
        this.title = title;
        this.linkUrl = linkUrl;
    }

    public static Link create(String title, String linkUrl) {
        return new Link(title, linkUrl);
    }

    public void update(String title, String linkUrl) {
        this.title = title;
        this.linkUrl = linkUrl;
    }
}
