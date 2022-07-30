package org.zerock.ex2.guestbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.ex2.guestbook.entity.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
}
