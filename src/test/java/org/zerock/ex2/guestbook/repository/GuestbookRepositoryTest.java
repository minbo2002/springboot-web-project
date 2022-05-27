package org.zerock.ex2.guestbook.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.ex2.guestbook.entity.GuestBook;
import org.zerock.ex2.guestbook.entity.QGuestBook;

import java.util.Optional;
import java.util.stream.IntStream;


@SpringBootTest
class GuestbookRepositoryTest {

    @Autowired
    private GuestbookRepository guestbookRepository;

    @Test  // 등록 테스트
    public void insertDummy() {
        IntStream.rangeClosed(1, 300).forEach(i -> {

            GuestBook guestBook = GuestBook.builder()
                    .title("Title.." + i)
                    .content("Content.." + i)
                    .writer("user" + (i % 10) )
                    .build();

            System.out.println(guestbookRepository.save(guestBook));
        });
    }

    @Test  // 수정 테스트
    public void updateTest() {
        Optional<GuestBook> result = guestbookRepository.findById(300L);

        if(result.isPresent()) {

            GuestBook guestBook = result.get();

            guestBook.changeTitle("Change Title");
            guestBook.changeContent("Change Content");

            guestbookRepository.save(guestBook);
        }
    }

    @Test  // 단일항목 검색 테스트
    public void testSingleSearchQuery() {
        Pageable pageable = PageRequest.of(0,10, Sort.by("gno").descending());

        QGuestBook qGuestBook = QGuestBook.guestBook;   // 1

        String keyword = "1";

        BooleanBuilder builder = new BooleanBuilder();  // 2

        BooleanExpression expression = qGuestBook.title.contains(keyword);  // 3

        builder.and(expression);  // 4

        Page<GuestBook> result = guestbookRepository.findAll(builder, pageable);

        result.stream().forEach(guestBook -> {
            System.out.println(guestBook);
        });
    }

    @Test  // 여러항목 검색 테스트
    public void testMultiSearchQuery() {
        Pageable pageable = PageRequest.of(0,10,Sort.by("gno").descending());

        QGuestBook qGuestBook = QGuestBook.guestBook;

        String keyword = "1";

        BooleanBuilder builder = new BooleanBuilder();

        BooleanExpression expressionTitle = qGuestBook.title.contains(keyword);
        BooleanExpression expressionContent = qGuestBook.content.contains(keyword);
        BooleanExpression expressionAll = expressionTitle.or(expressionContent);    // 1

        builder.and(expressionAll);  // 2

        builder.and(qGuestBook.gno.gt(0L));  // 3 (gno가 0L보다 크다라는 조건)

        Page<GuestBook> result = guestbookRepository.findAll(builder, pageable);

        result.stream().forEach(guestBook -> {
            System.out.println(guestBook);
        });

    }
}