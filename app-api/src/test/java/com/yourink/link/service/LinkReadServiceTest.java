package com.yourink.link.service;

import com.yourink.domain.link.Link;
import com.yourink.dto.api.ErrorCode;
import com.yourink.dto.link.LinkResponse;
import com.yourink.exception.NotFoundException;
import com.yourink.repository.link.LinkRepository;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@ActiveProfiles("test")
@SpringBootTest
class LinkReadServiceTest {
    @Autowired
    private LinkReadService linkReadService;

    @Autowired
    private LinkRepository linkRepository;

    @AfterEach
    void tearDown() {
        linkRepository.deleteAllInBatch();
    }

    @Nested
    @DisplayName("링크 리스트 조회 테스트")
    class GetLinksTest {
        @DisplayName("다수의 링크를 페이지네이션을 통해 조회한다.")
        @Test
        void get_links_by_id() {
            // given
            List<Link> linksToSave = IntStream
                    .rangeClosed(1, 10)
                    .mapToObj(index -> Link.create("타이틀-" + index, "https://www.naver.com/" + index))
                    .toList();

            List<Link> links = linkRepository.saveAll(linksToSave);

            // when
            int size = 5;
            var result = linkReadService.getALlLinksByIdDesc(links.get(5)
                                                                  .getId(), size);

            // then
            assertThat(result.data()
                             .size()).isEqualTo(size);
        }

        @DisplayName("링크의 ID를 기준으로 내림차순으로 정렬한다.")
        @Test
        void get_links_by_id_order_by_desc() {
            // given
            List<Link> linksToSave = IntStream
                    .rangeClosed(1, 10)
                    .mapToObj(index -> Link.create("타이틀-" + index, "https://www.naver.com/" + index))
                    .toList();

            List<Link> links = linkRepository.saveAll(linksToSave);

            // when
            int size = 5;
            var result = linkReadService.getALlLinksByIdDesc(links.get(5)
                                                                  .getId(), size);

            // then
            assertThat(isListInDescendingOrder(result.data())).isTrue();
        }


        private boolean isListInDescendingOrder(List<LinkResponse> list) {
            return IntStream.range(0, list.size() - 1)
                            .allMatch(i -> list.get(i)
                                               .id() >= list.get(i + 1)
                                                            .id());
        }

        @DisplayName("ID가 null일 경우에는 최신순으로 파라미터로 받은 사이즈만큼의 링크를 조회한다.")
        @Test
        void get_links_by_id_order_by_desc_when_id_is_null() {
            // given
            List<Link> linksToSave = IntStream
                    .rangeClosed(1, 10)
                    .mapToObj(index -> Link.create("타이틀-" + index, "https://www.naver.com/" + index))
                    .toList();

            List<Link> links = linkRepository.saveAll(linksToSave);

            // when
            int size = 5;
            var result = linkReadService.getALlLinksByIdDesc(null, size);

            // then
            assertThat(result.data()
                             .get(0)
                             .id()).isEqualTo(links.get(links.size() - 1)
                                                   .getId());
            assertThat(result.data()
                             .size()).isEqualTo(size);
        }

        @DisplayName("(cursorId = null 인 경우) - 조회 후 더 조회할 링크가 있으면 hasNext 필드에 true를 반환한다.")
        @Test
        void get_links_by_id_order_by_desc_has_next_when_cursor_id_null() {
            // given
            List<Link> linksToSave = IntStream
                    .rangeClosed(1, 10)
                    .mapToObj(index -> Link.create("타이틀-" + index, "https://www.naver.com/" + index))
                    .toList();

            linkRepository.saveAll(linksToSave);

            // when
            int size = 5;
            var result = linkReadService.getALlLinksByIdDesc(null, size);

            // then
            assertThat(result.hasNext()).isTrue();
        }

        @DisplayName("(cursorId != null 인 경우) - 조회 후 더 조회할 링크가 있으면 hasNext 필드에 true를 반환한다.")
        @Test
        void get_links_by_id_order_by_desc_has_next_when_cursor_id_not_null() {
            // given
            List<Link> linksToSave = IntStream
                    .rangeClosed(1, 10)
                    .mapToObj(index -> Link.create("타이틀-" + index, "https://www.naver.com/" + index))
                    .toList();

            List<Link> links = linkRepository.saveAll(linksToSave);
            links.forEach(link -> System.out.println(link.getId()));
            System.out.println(links.get(5)
                                    .getId());

            // when
            int size = 5;
            var result = linkReadService.getALlLinksByIdDesc(links.get(9)
                                                                  .getId(), size);

            // then
            assertThat(result.hasNext()).isTrue();
        }

        @DisplayName("(cursorId = null 인 경우) - 조회 후 더 조회할 링크가 없으면 hasNext 필드에 false를 반환한다.")
        @Test
        void get_links_by_id_order_by_desc_has_next_false_when_cursor_id_null() {
            // given
            List<Link> linksToSave = IntStream
                    .rangeClosed(1, 3)
                    .mapToObj(index -> Link.create("타이틀-" + index, "https://www.naver.com/" + index))
                    .toList();

            linkRepository.saveAll(linksToSave);

            // when
            int size = 5;
            var result = linkReadService.getALlLinksByIdDesc(null, size);

            // then
            assertThat(result.hasNext()).isFalse();
        }

        @DisplayName("(cursorId != null 인 경우) - 조회 후 더 조회할 링크가 없으면 hasNext 필드에 false를 반환한다.")
        @Test
        void get_links_by_id_order_by_desc_has_next_false_when_cursor_id_not_null() {
            // given
            List<Link> linksToSave = IntStream
                    .rangeClosed(1, 10)
                    .mapToObj(index -> Link.create("타이틀-" + index, "https://www.naver.com/" + index))
                    .toList();

            linkRepository.saveAll(linksToSave);

            // when
            int size = 5;
            var result = linkReadService.getALlLinksByIdDesc(1L, size);

            // then
            assertThat(result.hasNext()).isFalse();
        }
    }

    @Nested
    @DisplayName("단일 링크 조회 테스트")
    class getLinkTest {
        @DisplayName("단일 링크를 id를 이용해 조회한다.")
        @Test
        void get_link_by_id() {
            // given
            List<Link> linksToSave = IntStream
                    .rangeClosed(1, 10)
                    .mapToObj(index -> Link.create("타이틀-" + index, "https://www.naver.com/" + index))
                    .toList();

            List<Link> links = linkRepository.saveAll(linksToSave);

            // when
            int linkIdToGet = 5;
            var result = linkReadService.getLink(links.get(linkIdToGet)
                                                      .getId());

            // then
            assertThat(result.id()).isEqualTo(links.get(linkIdToGet)
                                                   .getId());
        }

        @DisplayName("해당 하는 id의 링크가 없을 경우 예외를 발생시킨다.")
        @Test
        void get_link_by_id_not_found() {
            // given
            // when
            // then
            Long linkIdToGet = 5L;
            assertThatThrownBy(() -> linkReadService.getLink(linkIdToGet))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage(ErrorCode.NOT_FOUND.getMessage());
        }
    }
}