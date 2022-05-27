package com.app.e_library.persistence.pagination;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageRequest {

    private int page = 0;
    private int size = 20;
    private String sortBy = "id";
    private String sortDirection = "asc";

    public Pageable getPageable() {
        Sort sort = Sort.by(sortBy).ascending();
        if (sortDirection.equals("desc"))
            sort = sort.descending();
        return org.springframework.data.domain.PageRequest.of(page, size, sort);
    }

}
