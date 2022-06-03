package org.zerock.ex2.guestbook.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
@Getter
abstract class BaseEntity {

    @CreatedDate  // entity가 생성되어 저장될때 시간이 자동으로 저장
    @Column(name = "regdate", updatable = false)  // DB에 반영될때 regdate컬럼은 변경되지 않는다는 의미
    private LocalDateTime regDate;

    @LastModifiedDate  // 조회한 entity의 값이 변경될때 시간이 자동으로 저장
    @Column(name = "moddate")
    private LocalDateTime modDate;
}
