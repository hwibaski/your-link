package com.yourink.link.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.yourink.domain.link.Link;
import com.yourink.dto.link.LinkResponse;
import com.yourink.exception.NotFoundException;
import com.yourink.repository.link.LinkRepository;

@SpringBootTest
class LinkServiceTest {
    @Autowired
    private LinkService linkService;

    @Autowired
    private LinkRepository linkRepository;

    @AfterEach
    void tearDown() {
        linkRepository.deleteAllInBatch();
    }

    @Nested
    @DisplayName("링크 생성 테스트")
    class CreateLinkTest {
        @DisplayName("링크를 생성한다.")
        @Test
        void create_link() {
            // given
            String title = "타이틀";
            String linkUrl = "https://www.naver.com";

            // when
            var result = linkService.createLink(title, linkUrl);

            // then
            assertThat(result.id()).isNotNull();
            assertThat(result.title()).isEqualTo("타이틀");
            assertThat(result.linkUrl()).isEqualTo("https://www.naver.com");
        }
    }

    @Nested
    @DisplayName("링크 수정 테스트")
    class UpdateLinkTest {
        @DisplayName("링크를 수정한다.")
        @Test
        void update_link_success() {
            // given
            String titleBeforeUpdate = "변경 전 타이틀";
            String linkUrlBeforeUpdate = "https://www.naver.com";

            var linkToSave = new Link(titleBeforeUpdate, linkUrlBeforeUpdate);
            var savedLink = linkRepository.save(linkToSave);

            // when
            String titleAfterUpdate = "변경 후 타이틀";
            String linkUrlAfterUpdate = "https://www.google.com";
            var result = linkService.updateLink(savedLink.getId(), titleAfterUpdate, linkUrlAfterUpdate);

            // then
            assertThat(result.title()).isEqualTo(titleAfterUpdate);
            assertThat(result.linkUrl()).isEqualTo(linkUrlAfterUpdate);
        }

        @DisplayName("수정하고자 하는 링크가 없을 경우 예외를 발생시킨다.")
        @Test
        void update_link_when_not_found() {
            // given
            // when

            // then
            Long id = 1L;
            String titleAfterUpdate = "변경 후 타이틀";
            String linkUrlAfterUpdate = "https://www.google.com";

            assertThatThrownBy(() -> linkService.updateLink(id, titleAfterUpdate, linkUrlAfterUpdate))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage("요청한 자원을 찾을 수 없습니다");
        }
    }

    @Nested
    @DisplayName("링크 조회 테스트")
    class GetLinkTest {
        // TODO : 테스트 코드 확인 및 다듬기
        @DisplayName("다수의 링크를 페이지네이션을 통해 조회한다.")
        @Test
        void get_links_by_id() {
            // given
            List<Link> linksToSave = IntStream
                    .rangeClosed(1, 10)
                    .mapToObj(index -> new Link("타이틀-" + index, "https://www.naver.com/" + index))
                    .toList();

            List<Link> links = linkRepository.saveAll(linksToSave);

            // when
            int size = 5;
            var result = linkService.getALlLinksByIdDesc(links.get(5).getId(), size);

            // then
            assertThat(result.data().size()).isEqualTo(size);
        }

        @DisplayName("링크의 ID를 기준으로 내림차순으로 정렬한다.")
        @Test
        void get_links_by_id_order_by_desc() {
            // given
            List<Link> linksToSave = IntStream
                    .rangeClosed(1, 10)
                    .mapToObj(index -> new Link("타이틀-" + index, "https://www.naver.com/" + index))
                    .toList();

            List<Link> links = linkRepository.saveAll(linksToSave);

            // when
            int size = 5;
            var result = linkService.getALlLinksByIdDesc(links.get(5).getId(), size);

            // then
            assertThat(isListInDescendingOrder(result.data())).isTrue();
        }


        private boolean isListInDescendingOrder(List<LinkResponse> list) {
            return IntStream.range(0, list.size() - 1)
                            .allMatch(i -> list.get(i).id() >= list.get(i + 1).id());
        }

        @DisplayName("ID가 null일 경우에는 최신순으로 파라미터로 받은 사이즈만큼의 링크를 조회한다.")
        @Test
        void get_links_by_id_order_by_desc_when_id_is_null() {
            // given
            List<Link> linksToSave = IntStream
                    .rangeClosed(1, 10)
                    .mapToObj(index -> new Link("타이틀-" + index, "https://www.naver.com/" + index))
                    .toList();

            List<Link> links = linkRepository.saveAll(linksToSave);

            // when
            int size = 5;
            var result = linkService.getALlLinksByIdDesc(null, size);

            // then
            assertThat(result.data().get(0).id()).isEqualTo(links.get(links.size() - 1).getId());
            assertThat(result.data().size()).isEqualTo(size);
        }

        @DisplayName("(cursorId = null 인 경우) - 조회 후 더 조회할 링크가 있으면 hasNext 필드에 true를 반환한다.")
        @Test
        void get_links_by_id_order_by_desc_has_next_when_cursor_id_null() {
            // given
            List<Link> linksToSave = IntStream
                    .rangeClosed(1, 10)
                    .mapToObj(index -> new Link("타이틀-" + index, "https://www.naver.com/" + index))
                    .toList();

            linkRepository.saveAll(linksToSave);

            // when
            int size = 5;
            var result = linkService.getALlLinksByIdDesc(null, size);

            // then
            assertThat(result.hasNext()).isTrue();
        }

        @DisplayName("(cursorId != null 인 경우) - 조회 후 더 조회할 링크가 있으면 hasNext 필드에 true를 반환한다.")
        @Test
        void get_links_by_id_order_by_desc_has_next_when_cursor_id_not_null() {
            // given
            List<Link> linksToSave = IntStream
                    .rangeClosed(1, 10)
                    .mapToObj(index -> new Link("타이틀-" + index, "https://www.naver.com/" + index))
                    .toList();

            List<Link> links = linkRepository.saveAll(linksToSave);
            links.forEach(link -> System.out.println(link.getId()));
            System.out.println(links.get(5).getId());

            // when
            int size = 5;
            var result = linkService.getALlLinksByIdDesc(links.get(9).getId(), size);

            // then
            assertThat(result.hasNext()).isTrue();
        }

        @DisplayName("(cursorId = null 인 경우) - 조회 후 더 조회할 링크가 없으면 hasNext 필드에 false를 반환한다.")
        @Test
        void get_links_by_id_order_by_desc_has_next_false_when_cursor_id_null() {
            // given
            List<Link> linksToSave = IntStream
                    .rangeClosed(1, 3)
                    .mapToObj(index -> new Link("타이틀-" + index, "https://www.naver.com/" + index))
                    .toList();

            linkRepository.saveAll(linksToSave);

            // when
            int size = 5;
            var result = linkService.getALlLinksByIdDesc(null, size);

            // then
            assertThat(result.hasNext()).isFalse();
        }

        @DisplayName("(cursorId != null 인 경우) - 조회 후 더 조회할 링크가 없으면 hasNext 필드에 false를 반환한다.")
        @Test
        void get_links_by_id_order_by_desc_has_next_false_when_cursor_id_not_null() {
            // given
            List<Link> linksToSave = IntStream
                    .rangeClosed(1, 10)
                    .mapToObj(index -> new Link("타이틀-" + index, "https://www.naver.com/" + index))
                    .toList();

            linkRepository.saveAll(linksToSave);

            // when
            int size = 5;
            var result = linkService.getALlLinksByIdDesc(1L, size);

            // then
            assertThat(result.hasNext()).isFalse();
        }
    }
}
