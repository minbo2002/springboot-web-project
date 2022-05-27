package org.zerock.ex2.guestbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.zerock.ex2.guestbook.entity.GuestBook;

public interface GuestbookRepository extends JpaRepository<GuestBook, Long>,
        QuerydslPredicateExecutor<GuestBook> {
}
