package org.zerock.ex2.guestbook.service;

import org.zerock.ex2.guestbook.dto.GuestBookDTO;
import org.zerock.ex2.guestbook.dto.PageRequestDTO;
import org.zerock.ex2.guestbook.dto.PageResultDTO;
import org.zerock.ex2.guestbook.entity.GuestBook;

public interface GuestBookService {

    Long register(GuestBookDTO guestBookDTO);

    PageResultDTO<GuestBookDTO, GuestBook> getList(PageRequestDTO pageRequestDTO);



    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ//



    default GuestBook dtoToEntity(GuestBookDTO dto) {

        GuestBook entity = GuestBook.builder()
                .gno(dto.getGno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();

        return entity;
    }

    default GuestBookDTO entityToDto(GuestBook entity) {

        GuestBookDTO dto = GuestBookDTO.builder()
                .gno(entity.getGno())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writer(entity.getWriter())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();

        return dto;
    }
}
