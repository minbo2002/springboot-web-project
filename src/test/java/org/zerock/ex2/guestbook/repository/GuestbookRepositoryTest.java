package org.zerock.ex2.guestbook.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.zerock.ex2.guestbook.entity.GuestBook;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GuestbookRepositoryTest {

    @Autowired
    private GuestbookRepository guestbookRepository;

    @Test
    public void InsertDummy() {
        IntStream.rangeClosed(1, 300).forEach(i -> {

            GuestBook guestBook = GuestBook.builder()
                    .title("Title.." + i)
                    .content("Content.." + i)
                    .writer("user" + (i % 10) )
                    .build();

            System.out.println(guestbookRepository.save(guestBook));
        });
    }

    @Test
    public void UpdateTest() {
        Optional<GuestBook> result = guestbookRepository.findById(300L);

        if(result.isPresent()) {

            GuestBook guestBook = result.get();

            guestBook.changeTitle("Change Title");
            guestBook.changeContent("Change Content");

            guestbookRepository.save(guestBook);
        }
    }
}