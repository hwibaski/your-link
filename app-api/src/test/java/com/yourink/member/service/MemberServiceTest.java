package com.yourink.member.service;

import com.yourink.repository.member.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MemberServiceTest {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
    }

    @Nested
    @DisplayName("멤버 생성 테스트")
    class CreateMemberTest {
        @DisplayName("멤버를 생성한다")
        @Test
        void create_member() {
            // given
            String email = "temp@gmail.com";

            // when
            var result = memberService.createMember(email);

            // then
            assertThat(result.id()).isNotNull();
            assertThat(result.email()).isEqualTo(email);
        }
    }
}
