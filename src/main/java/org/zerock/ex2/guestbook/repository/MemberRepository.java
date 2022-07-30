package org.zerock.ex2.guestbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.ex2.guestbook.entity.Member;

public interface MemberRepository extends JpaRepository<Member, String> {
}
