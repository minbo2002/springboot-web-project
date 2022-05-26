package org.zerock.ex2.sample.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.ex2.sample.entity.Memo;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {

    List<Memo> findByMnoBetweenOrderByMnoDesc(Long from, Long to);  // 쿼리메서드 (특정범위 정렬)

    Page<Memo> findByMnoBetween(Long from, Long to, Pageable pageable);  // Pageable 인터페이스 결합

    void deleteMemoByMnoLessThan(Long num);  // 쿼리메서드 (특정범위 삭제)
}
