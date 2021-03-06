package org.zerock.ex2.guestbook.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
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
import org.zerock.ex2.guestbook.entity.QGuestBook;
import org.zerock.ex2.guestbook.repository.GuestbookRepository;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class GuestBookServiceImpl implements GuestBookService {

    private final GuestbookRepository guestbookRepository;

    // 글 등록
    @Override
    public Long register(GuestBookDTO guestBookDTO) {

        log.info("DTO-----------------------");
        log.info(guestBookDTO);

        GuestBook entity = dtoToEntity(guestBookDTO);

        log.info(entity);

        GuestBook result = guestbookRepository.save(entity);

        return result.getGno();
    }

    // 글 목록
    @Override
    public PageResultDTO<GuestBookDTO, GuestBook> getList(PageRequestDTO pageRequestDTO) {

        Pageable pageable = pageRequestDTO.getPageble(Sort.by("gno").descending());

        // 검색 조건
        BooleanBuilder booleanBuilder = getSearch(pageRequestDTO);

        Page<GuestBook> result = guestbookRepository.findAll(booleanBuilder, pageable);

        /* 리팩토링
        public Page<GuestBookDTO> getList(PageRequestDTO pageRequestDTO) {

        Page<GuestBookDTO> map = guestbookRepository.findAll(booleanBuilder, pageable).map(this::entityToDto);

        return map;
        }
         */

        Function<GuestBook, GuestBookDTO> fn = (entity -> entityToDto(entity));

//      PageResultDTO<GuestBookDTO, GuestBook> pageResultDTO = new PageResultDTO<>(result, fn);
//      return pageResultDTO;
        return new PageResultDTO<>(result, fn);
//      PageResultDTO에 JPA의 처리결과인 Page<Entity>와 Function을 전달해서  entity객체를 -> DTO의 list로 변환
    }

    // 글 조회
    @Override
    public GuestBookDTO read(Long gno) {

        Optional<GuestBook> result = guestbookRepository.findById(gno);

        return result.isPresent() ? entityToDto(result.get()) : null;
    }

    // 글 삭제
    @Override
    public void remove(Long gno) {

        guestbookRepository.deleteById(gno);
    }

    // 글 수정
    @Override
    public void modify(GuestBookDTO guestBookDTO) {

        Optional<GuestBook> result = guestbookRepository.findById(guestBookDTO.getGno());

        if(result.isPresent()) {

            GuestBook entity = result.get();

            entity.changeTitle(guestBookDTO.getTitle());
            entity.changeContent(guestBookDTO.getContent());

            guestbookRepository.save(entity);
        }
    }

    private BooleanBuilder getSearch(PageRequestDTO pageRequestDTO) {
        // Querydsl의 BooleanBuilder 클래스 사용

        String type = pageRequestDTO.getType();

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QGuestBook qGuestBook = QGuestBook.guestBook;

        String keyword = pageRequestDTO.getKeyword();

        BooleanExpression booleanExpression = qGuestBook.gno.gt(0L); // gno > 0 조건만 생성

        booleanBuilder.and(booleanExpression);

        // 검색조건 없는경우
        if(type==null || type.trim().length()==0) {
            return booleanBuilder;
        }

        // 검색조건 추가
        BooleanBuilder conditionBuilder = new BooleanBuilder();

        if(type.contains("t")) {
            conditionBuilder.or(qGuestBook.title.contains(keyword));
        }
        if(type.contains("c")) {
            conditionBuilder.or(qGuestBook.content.contains(keyword));
        }
        if(type.contains("w")) {
            conditionBuilder.or(qGuestBook.writer.contains(keyword));
        }
        
        // 모든 조건 통합
        booleanBuilder.and(conditionBuilder);
        
        return booleanBuilder;
    }
}
