package org.zerock.ex2.guestbook.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.ex2.guestbook.dto.GuestBookDTO;
import org.zerock.ex2.guestbook.dto.PageRequestDTO;
import org.zerock.ex2.guestbook.dto.PageResultDTO;
import org.zerock.ex2.guestbook.entity.GuestBook;


@SpringBootTest
class GuestBookServiceTest {

    @Autowired
    GuestBookService guestBookService;

    @Test  // 등록 테스트
    public void testRegister() {

        GuestBookDTO guestBookDTO = GuestBookDTO.builder()
                .title("Sample Title")
                .content("Sample Content")
                .writer("user0")
                .build();

        System.out.println(guestBookService.register(guestBookDTO));
    }


    @Test  // 목록(list) 테스트
    public void testList() {

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .build();

        PageResultDTO<GuestBookDTO, GuestBook> resultDTO = guestBookService.getList(pageRequestDTO);


        System.out.println("prev : " + resultDTO.isPrev());
        System.out.println("next : " + resultDTO.isNext());
        System.out.println("total : " + resultDTO.getTotalPage());

        System.out.println("======================================");
        for(GuestBookDTO guestBookDTO : resultDTO.getDtoList()) {

            System.out.println(guestBookDTO);
        }

        System.out.println("======================================");
        resultDTO.getPageList().forEach(i -> System.out.println(i));
    }
}