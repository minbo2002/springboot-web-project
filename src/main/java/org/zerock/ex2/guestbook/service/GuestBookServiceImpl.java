package org.zerock.ex2.guestbook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.ex2.guestbook.dto.GuestBookDTO;
import org.zerock.ex2.guestbook.dto.PageRequestDTO;
import org.zerock.ex2.guestbook.dto.PageResultDTO;
import org.zerock.ex2.guestbook.entity.GuestBook;
import org.zerock.ex2.guestbook.repository.GuestbookRepository;

import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class GuestBookServiceImpl implements GuestBookService {

    private final GuestbookRepository guestbookRepository;

    @Override
    public Long register(GuestBookDTO guestBookDTO) {

        log.info("DTO-----------------------");
        log.info(guestBookDTO);

        GuestBook entity = dtoToEntity(guestBookDTO);

        log.info(entity);

        GuestBook result = guestbookRepository.save(entity);

        return result.getGno();
    }

    @Override
    public PageResultDTO<GuestBookDTO, GuestBook> getList(PageRequestDTO pageRequestDTO) {

        Pageable pageable = pageRequestDTO.getPageble(Sort.by("gno").descending());

        Page<GuestBook> result = guestbookRepository.findAll(pageable);

        Function<GuestBook, GuestBookDTO> fn = (entity -> entityToDto(entity));

//        PageResultDTO<GuestBookDTO, GuestBook> pageResultDTO = new PageResultDTO<>(result, fn);
     // PageResultDTO에 JPA의 처리결과인 Page<Entity>와 Function을 전달해서  entity객체를 -> DTO의 list로 변환

//        return pageResultDTO;
        return new PageResultDTO<>(result, fn);
    }
}
