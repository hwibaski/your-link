package com.yourink.linkStat.service;

import com.yourink.domain.link.Link;
import com.yourink.domain.member.Member;
import com.yourink.repository.link.LinkRepository;
import com.yourink.repository.member.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("test")
@SpringBootTest
class LinkStatReadServiceTest {
    @Autowired
    private LinkStatReadService linkStatReadService;

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
        linkRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @Nested()
    @DisplayName("총 링크의 갯수 조회 테스트")
    class LinkStatReadTest {
        @DisplayName("링크 통계를 조회한다.")
        @Test
        void read_link_stat() {
            // given
            Member savedMember = memberRepository.save(Member.create("test@gmail.com"));
            List<Link> linksToSave = IntStream
                    .rangeClosed(1, 10)
                    .mapToObj(index -> Link.create("타이틀-" + index, "https://www.naver.com/" + index, savedMember))
                    .toList();

            List<Link> links = linkRepository.saveAll(linksToSave);

            // when
            var result = linkStatReadService.countAll();

            // then
            assertThat(result.count()).isEqualTo(links.size());
        }

        @DisplayName("링크가 하나도 생성되지 않았을 시 0을 리턴한다.")
        @Test
        void read_link_stat_when_link_count_zero() {
            // given
            // when
            var result = linkStatReadService.countAll();

            // then
            assertThat(result.count()).isEqualTo(0L);
        }
    }
}
