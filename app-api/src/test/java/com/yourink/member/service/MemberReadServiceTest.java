package com.yourink.member.service;

import com.yourink.domain.member.Member;
import com.yourink.exception.NotFoundException;
import com.yourink.repository.member.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@ActiveProfiles("test")
@SpringBootTest
class MemberReadServiceTest {
    @Autowired
    private MemberReadService memberReadService;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
    }

    @Nested
    @DisplayName("id로 회원 조회")
    class GetMemberById {
        @Test
        @DisplayName("회원 조회 성공")
        void getMemberById_success() {
            // given
            var savedMember = memberRepository.save(Member.create("temp@gmail.com"));

            // when
            var result = memberReadService.getMemberById(savedMember.getId());

            // then
            assertSoftly(s -> {
                s.assertThat(result).isNotNull();
                s.assertThat(result.getId()).isEqualTo(savedMember.getId());
                s.assertThat(result.getEmail()).isEqualTo(savedMember.getEmail());
            });
        }

        @Test
        @DisplayName("회원 조회 실패 - 존재하지 않는 회원")
        void getMemberById_fail_not_found() {
            // given
            var notFoundMemberId = 1L;

            // when & then
            assertThatThrownBy(() -> memberReadService.getMemberById(notFoundMemberId))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("요청한 자원을 찾을 수 없습니다");
        }
    }

    @Nested
    @DisplayName("email로 회원 조회")
    class GetMemberByEmail {
        @Test
        @DisplayName("회원 조회 성공")
        void getMemberByEmail_success() {
            // given
            var savedMember = memberRepository.save(Member.create("temp@gmail.com"));

            // when
            var result = memberReadService.getMemberByEmail(savedMember.getEmail());

            // then
            assertSoftly(s -> {
                s.assertThat(result).isNotNull();
                s.assertThat(result.getId()).isEqualTo(savedMember.getId());
                s.assertThat(result.getEmail()).isEqualTo(savedMember.getEmail());
            });
        }

        @Test
        @DisplayName("회원 조회 실패 - 존재하지 않는 회원")
        void getMemberById_fail_not_found() {
            // given
            var notFoundMemberEmail = "1234@gmail.com";

            // when & then
            assertThatThrownBy(() -> memberReadService.getMemberByEmail(notFoundMemberEmail))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("요청한 자원을 찾을 수 없습니다");
        }
    }
}
