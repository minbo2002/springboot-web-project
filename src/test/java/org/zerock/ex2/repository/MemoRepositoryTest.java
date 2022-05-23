package org.zerock.ex2.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.ex2.entity.Memo;

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
    @Test
    public void testSelectOne() {
        Long mno = 100L;  // DB에 존재하는 PK mno

        Memo memo = memoRepository.getReferenceById(mno);
        System.out.println("=======================================");

        System.out.println(memo);
    }

}