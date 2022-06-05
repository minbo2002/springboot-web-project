package org.zerock.ex2.sample.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.ex2.guestbook.entity.GuestBook;
import org.zerock.ex2.guestbook.repository.GuestbookRepository;

import java.util.List;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DsgRepositoryTest {

    @Autowired
    private GuestbookRepository guestbookRepository;

    private GuestBook guestBook;

    @BeforeEach
    void setup() {
        guestBook = GuestBook.builder()
                .writer("dsg")
                .title("dsg_title")
                .content("con")
                .build();
    }

    @Test
    public void test() {

        guestbookRepository.save(guestBook);

        List<GuestBook> all = guestbookRepository.findAll();

        assertThat(all.size()).isEqualTo(1);
    }

    @Test
    public void page() {
        // given
        LongStream.rangeClosed(1, 20).forEach(i -> {
            guestBook = GuestBook.builder()
                    .writer("dsg" + i)
                    .title("dsg_title")
                    .content("con")
                    .build();
            guestbookRepository.save(guestBook);
        });

        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());

        // when
        Page<GuestBook> page = guestbookRepository.findAll(pageable);

        page.forEach(System.out::println);

        System.out.println("pageContent: " + page.getContent());
        System.out.println("getPageable: " + page.getPageable());
        System.out.println("getTotalElements: " + page.getTotalElements());
        System.out.println("getTotalPages: " + page.getTotalPages());

        // then
        assertThat(page.getTotalElements()).isEqualTo(20);
        assertThat(page.getTotalPages()).isEqualTo(2);

    }
}
