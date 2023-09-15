package com.yourink.domain.link;

import com.yourink.domain.base.BaseAuditEntity;
import jakarta.persistence.Entity;

@Entity
public class Link extends BaseAuditEntity {
    private String title;

    private String linkUrl;

    private String thumbnailUrl;
}
