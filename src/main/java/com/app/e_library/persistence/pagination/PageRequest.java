package com.app.e_library.persistence.pagination;

import lombok.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Setter
public class PageRequest {

    private int page = 0;
    private int size = 20;
    private String sortBy = "id";
    private Sort.Direction sortDirection = Sort.Direction.ASC;

    public Pageable getPageable() {
        return org.springframework.data.domain.PageRequest.of(page, size, sortDirection, sortBy);
    }

}
