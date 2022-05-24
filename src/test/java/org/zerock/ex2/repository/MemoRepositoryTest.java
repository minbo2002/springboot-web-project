package org.zerock.ex2.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.ex2.entity.Memo;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;


@SpringBootTest
class MemoRepositoryTest {

    @Autowired
    MemoRepository memoRepository;

    @Test  // 의존성주입 테스트
    public void testClass() {  // MemoRepository가 정상적으로 처리되고 의존성 주입에 문제가 없는지 확인
        System.out.println(memoRepository.getClass().getName());
    }

    @Test  // 등록 테스트
    public void testInsertDummies() {  // 새로운 100개의 Memo객체를 생성하고 데이터를 insert
        IntStream.range(1,101).forEach(i -> {
            Memo memo = Memo.builder()
                    .memoText("Sample..." + i)
                    .build();

            memoRepository.save(memo);
        });
    }

    @Test  // 전체조회 테스트
    public void testSelectAll() {
        Long mno = 100L; // DB에 존재하는 PK mno

        Optional<Memo> result = memoRepository.findById(mno);  // .findById() 반환타입 Optional
        System.out.println("=======================================");

        if(result.isPresent()) {
            Memo memo = result.get();
            System.out.println(memo);
        }
    }

    @Transactional
    @Test  // 특정조회 테스트
    public void testSelectOne() {
        Long mno = 100L;  // DB에 존재하는 PK mno

        Memo memo = memoRepository.getReferenceById(mno);
        System.out.println("=======================================");

        System.out.println(memo);
    }

    @Transactional
    @Commit
    @Test  // 수정 테스트
    public void testUpdate() {
        Memo memo = Memo.builder()
                .mno(396L)
                .memoText("Update Text")
                .build();

        System.out.println(memoRepository.save(memo));
    }

    @Test  // 삭제 테스트
    public void testDelete() {
        Long mno = 399L;

        memoRepository.deleteById(mno);
    }

    @Test  // 페이징 처리 테스트
    public void testPageDefault() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<Memo> result = memoRepository.findAll(pageable);

        System.out.println(result);

        System.out.println("=========================================");

        System.out.println("Total Page : " + result.getTotalPages());  // 총 페이지 개수
        System.out.println("Total Count : " + result.getTotalElements());  // 전체 개수
        System.out.println("Page Number : " + result.getNumber());  // 현재 페이지 번호
        System.out.println("Page Size : " + result.getSize());  // 페이지당 데이터 개수
        System.out.println("has next page? : " + result.hasNext());  // 다음페이지 존재여부
        System.out.println("first page? : " + result.isFirst());  // 시작페이지(0) 여부

        System.out.println("=========================================");

        for(Memo memo : result.getContent()) {
            System.out.println(memo);
        }
    }

    @Test  // 페이지 정렬 테스트
    public void testPageSort() {
        Sort sort1 = Sort.by("mno").descending();  // PK인 mno를 역순(desc)으로 정렬
        Sort sort2 = Sort.by("memoText").ascending();  // memoText 컬럼을 순차적(asc)로 정렬
        Sort sortAll = sort1.and(sort2);

        Pageable pageable = PageRequest.of(0, 10, sortAll);
        Page<Memo> result = memoRepository.findAll(pageable);

        result.get().forEach(memo -> {
            System.out.println(memo);
        });
    }

    @Test  // 쿼리메서드 테스트
    public void testQueryMethod() {
        List<Memo> list = memoRepository.findByMnoBetweenOrderByMnoDesc(300L, 310L);

        for(Memo memo : list) {
            System.out.println(memo);
        }
    }

    @Test  // 쿼리메서드 + Pageable인터페이스 테스트
    public void testQueryMethodWithPageable() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());
        Page<Memo> result = memoRepository.findByMnoBetween(300L, 310L, pageable);

        result.get().forEach(memo -> System.out.println(memo));
    }

    @Transactional
    @Commit
    @Test
    public void testQueryMethodDelete() {

        memoRepository.deleteMemoByMnoLessThan(398L);
    }
}