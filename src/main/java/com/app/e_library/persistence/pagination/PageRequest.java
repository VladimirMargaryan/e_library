package com.app.e_library.persistence.pagination;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


public class PageRequest {

    public static Pageable buildPage(int page,
                                     int size,
                                     String sortBy,
                                     String sortDirection) {

        Sort sort = Sort.by(sortBy).ascending();

        if (sortDirection.equals("desc"))
            sort = sort.descending();
        return org.springframework.data.domain.PageRequest.of(page, size, sort);
    }

}
