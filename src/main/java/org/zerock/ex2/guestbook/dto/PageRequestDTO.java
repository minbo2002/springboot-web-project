package org.zerock.ex2.guestbook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO {

    private int page;
    private int size;

    private String type;  // 검색 조건
    private String keyword;  // 검색 키워드

    public PageRequestDTO() {
        this.page = 1;
        this.size = 10;
    }

    public Pageable getPageble(Sort sort) {

        return PageRequest.of(page-1, size, sort);
    }
}
