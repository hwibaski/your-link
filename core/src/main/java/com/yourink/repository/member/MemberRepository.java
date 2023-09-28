package com.yourink.repository.member;

import com.yourink.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);
}
