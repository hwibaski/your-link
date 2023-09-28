package com.yourink.domain.member;

import com.yourink.domain.base.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseAuditEntity {
    @Column(unique = true)
    private String email;

    private Member(String email) {
        this.email = email;
    }

    public static Member create(String email) {
        return new Member(email);
    }
}
