package com.yourink.domain.tag;

import com.yourink.domain.base.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag extends BaseAuditEntity {
    @Column(unique = true)
    private String name;

    private Tag(String name) {
        this.name = name;
    }

    public static Tag create(String name) {
        return new Tag(name);
    }
}
