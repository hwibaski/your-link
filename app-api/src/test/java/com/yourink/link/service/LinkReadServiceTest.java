package com.yourink.link.service;

import com.yourink.domain.link.Link;
import com.yourink.domain.member.Member;
import com.yourink.dto.api.ErrorCode;
import com.yourink.exception.NotFoundException;
import com.yourink.link.controller.dto.GetLinkListResponse;
import com.yourink.repository.link.LinkRepository;
import com.yourink.repository.member.MemberRepository;
import com.yourink.repository.tag.TagLinkMapRepository;
import com.yourink.repository.tag.TagRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@ActiveProfiles("test")
@SpringBootTest
class LinkReadServiceTest {
    @Autowired
    private LinkReadService linkReadService;

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TagLinkMapRepository tagLinkMapRepository;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
        tagLinkMapRepository.deleteAllInBatch();
        linkRepository.deleteAllInBatch();
        tagRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @Nested
    @DisplayName("링크 리스트 조회 테스트")
    class GetLinksTest {
        @Test
        @DisplayName("다수의 링크를 페이지네이션을 통해 조회한다.")
        void get_links_by_id() {
            // given
            Member savedMember = memberRepository.save(Member.create("test@gmail.com"));
            List<Link> linksToSave = IntStream
                    .rangeClosed(1, 10)
                    .mapToObj(index -> Link.create("타이틀-" + index, "https://www.naver.com/" + index, savedMember))
                    .toList();

            List<Link> links = linkRepository.saveAll(linksToSave);

            // when
            int size = 5;
            var result = linkReadService.getALlLinksByIdAndMemberIdDesc(links.get(5)
                                                                             .getId(), size, savedMember.getId());

            // then
            assertThat(result.data()
                             .size()).isEqualTo(size);
        }

        @Test
        @DisplayName("링크의 ID를 기준으로 내림차순으로 정렬한다.")
        void get_links_by_id_order_by_desc() {
            // given
            Member savedMember = memberRepository.save(Member.create("test@gmail.com"));
            List<Link> linksToSave = IntStream
                    .rangeClosed(1, 10)
                    .mapToObj(index -> Link.create("타이틀-" + index, "https://www.naver.com/" + index, savedMember))
                    .toList();

            List<Link> links = linkRepository.saveAll(linksToSave);

            // when
            int size = 5;
            var result = linkReadService.getALlLinksByIdAndMemberIdDesc(links.get(5)
                                                                             .getId(), size, savedMember.getId());

            // then
            assertThat(isListInDescendingOrder(result.data())).isTrue();
        }


        private boolean isListInDescendingOrder(List<GetLinkListResponse> list) {
            return IntStream.range(0, list.size() - 1)
                            .allMatch(i -> list.get(i)
                                               .id() >= list.get(i + 1)
                                                            .id());
        }

        @Test
        @DisplayName("ID가 null일 경우에는 최신순으로 파라미터로 받은 사이즈만큼의 링크를 조회한다.")
        void get_links_by_id_order_by_desc_when_id_is_null() {
            // given
            Member savedMember = memberRepository.save(Member.create("test@gmail.com"));
            List<Link> linksToSave = IntStream
                    .rangeClosed(1, 10)
                    .mapToObj(index -> Link.create("타이틀-" + index, "https://www.naver.com/" + index, savedMember))
                    .toList();

            List<Link> links = linkRepository.saveAll(linksToSave);

            // when
            int size = 5;
            var result = linkReadService.getALlLinksByIdAndMemberIdDesc(null, size, savedMember.getId());

            // then
            assertThat(result.data()
                             .get(0)
                             .id()).isEqualTo(links.get(links.size() - 1)
                                                   .getId());
            assertThat(result.data()
                             .size()).isEqualTo(size);
        }

        @Test
        @DisplayName("(cursorId = null 인 경우) - 조회 후 더 조회할 링크가 있으면 hasNext 필드에 true를 반환한다.")
        void get_links_by_id_order_by_desc_has_next_when_cursor_id_null() {
            // given
            Member savedMember = memberRepository.save(Member.create("test@gmail.com"));
            List<Link> linksToSave = IntStream
                    .rangeClosed(1, 10)
                    .mapToObj(index -> Link.create("타이틀-" + index, "https://www.naver.com/" + index, savedMember))
                    .toList();

            linkRepository.saveAll(linksToSave);

            // when
            int size = 5;
            var result = linkReadService.getALlLinksByIdAndMemberIdDesc(null, size, savedMember.getId());

            // then
            assertThat(result.hasNext()).isTrue();
        }

        @Test
        @DisplayName("(cursorId != null 인 경우) - 조회 후 더 조회할 링크가 있으면 hasNext 필드에 true를 반환한다.")
        void get_links_by_id_order_by_desc_has_next_when_cursor_id_not_null() {
            // given
            Member savedMember = memberRepository.save(Member.create("test@gmail.com"));
            List<Link> linksToSave = IntStream
                    .rangeClosed(1, 10)
                    .mapToObj(index -> Link.create("타이틀-" + index, "https://www.naver.com/" + index, savedMember))
                    .toList();

            List<Link> links = linkRepository.saveAll(linksToSave);

            // when
            int size = 5;
            var result = linkReadService.getALlLinksByIdAndMemberIdDesc(links.get(9)
                                                                             .getId(), size, savedMember.getId());

            // then
            assertThat(result.hasNext()).isTrue();
        }

        @Test
        @DisplayName("(cursorId = null 인 경우) - 조회 후 더 조회할 링크가 없으면 hasNext 필드에 false를 반환한다.")
        void get_links_by_id_order_by_desc_has_next_false_when_cursor_id_null() {
            // given
            Member savedMember = memberRepository.save(Member.create("test@gmail.com"));
            List<Link> linksToSave = IntStream
                    .rangeClosed(1, 3)
                    .mapToObj(index -> Link.create("타이틀-" + index, "https://www.naver.com/" + index, savedMember))
                    .toList();

            linkRepository.saveAll(linksToSave);

            // when
            int size = 5;
            var result = linkReadService.getALlLinksByIdAndMemberIdDesc(null, size, savedMember.getId());

            // then
            assertThat(result.hasNext()).isFalse();
        }

        @Test
        @DisplayName("(cursorId != null 인 경우) - 조회 후 더 조회할 링크가 없으면 hasNext 필드에 false를 반환한다.")
        void get_links_by_id_order_by_desc_has_next_false_when_cursor_id_not_null() {
            // given
            Member savedMember = memberRepository.save(Member.create("test@gmail.com"));
            List<Link> linksToSave = IntStream
                    .rangeClosed(1, 10)
                    .mapToObj(index -> Link.create("타이틀-" + index, "https://www.naver.com/" + index, savedMember))
                    .toList();

            linkRepository.saveAll(linksToSave);

            // when
            int size = 5;
            var result = linkReadService.getALlLinksByIdAndMemberIdDesc(1L, size, savedMember.getId());

            // then
            assertThat(result.hasNext()).isFalse();
        }
    }

    @Nested
    @DisplayName("단일 링크 조회 테스트")
    class getLinkTest {
        @Test
        @DisplayName("단일 링크를 id를 이용해 조회한다")
        void get_link_by_id_without_tag() {
            // given
            Member savedMember = memberRepository.save(Member.create("test@gmail.com"));
            var linkToSave = Link.create("타이틀-1", "https://www.naver.com/1", savedMember);

            Link savedLink = linkRepository.save(linkToSave);

            // when
            var result = linkReadService.getLink(savedLink.getId(), savedMember.getId());

            // then
            assertThat(result.id()).isEqualTo(savedLink.getId());
            assertThat(result.title()).isEqualTo(savedLink.getTitle());
            assertThat(result.linkUrl()).isEqualTo(savedLink.getLinkUrl());
        }

        @Test
        @DisplayName("해당 하는 id의 링크가 없을 경우 예외를 발생시킨다.")
        void get_link_by_id_not_found() {
            // given
            // when
            // then
            Long linkIdToGet = 5L;
            Long fakeMemberId = 1L;
            assertThatThrownBy(() -> linkReadService.getLink(linkIdToGet, fakeMemberId))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage(ErrorCode.NOT_FOUND.getMessage());
        }

        @Test
        @DisplayName("링크 조회수 증가 테스트")
        void get_link_increase_count() {
            // given
            Member savedMember = memberRepository.save(Member.create("test@gmail.com"));
            var link = linkRepository.save(Link.create("타이틀-1", "https://www.naver.com/1", savedMember));

            // when
            linkReadService.getLink(link.getId(), savedMember.getId());

            // then
            Optional<Link> byId = linkRepository.findById(link.getId());
            assertThat(byId.get().getHitCount()).isEqualTo(1);
        }
    }
}
