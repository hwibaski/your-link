package com.yourink.member.service;

import com.yourink.domain.member.Member;
import com.yourink.dto.api.ErrorCode;
import com.yourink.exception.DuplicatedResourceException;
import com.yourink.repository.member.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

        @DisplayName("중복된 email으로는 멤버를 생성할 수 없다")
        @Test
        void create_member_duplicated_email() {
            // given
            String email = "temp@gmail.com";
            memberRepository.save(Member.create(email));

            // when
            // then
            assertThatThrownBy(() -> memberService.createMember(email))
                    .isInstanceOf(DuplicatedResourceException.class)
                    .hasMessage(ErrorCode.DUPLICATED_RESOURCE.getMessage());
        }
    }
}
