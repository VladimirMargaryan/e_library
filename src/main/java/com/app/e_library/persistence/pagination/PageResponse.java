package com.app.e_library.persistence.pagination;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@Getter
public class PageResponse<T> {

    private final List<T> content;
    private final Integer totalPages;
    private final Long totalElements;

    @JsonIgnore
    private final HttpHeaders pageHeaders;


    public PageResponse(Page<T> page) {
        this.content = page.getContent();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.pageHeaders = buildPageHeaders(page);
    }

    private String constructNextPageUrl(Page<T> page){
        return page.hasNext() ? ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replaceQueryParam("page", page.getNumber() + 1)
                .build().toUriString() : null;
    }

    private String constructPreviousPageUrl(Page<T> page){
        return page.hasPrevious() ? ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replaceQueryParam("page", page.getNumber() - 1)
                .build().toUriString() : null;
    }

    private String constructFirstPageUrl(Page<T> page){
        return !page.isFirst() && page.hasPrevious() ? ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replaceQueryParam("page", 0)
                .build().toUriString() : null;
    }

    private String constructLastPageUrl(Page<T> page){
        return !page.isLast() && page.hasNext() ? ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replaceQueryParam("page", page.getTotalPages() - 1)
                .build().toUriString() : null;
    }

    private HttpHeaders buildPageHeaders(Page<T> page){

        String firstPageUrl = constructFirstPageUrl(page);
        String lastPageUrl = constructLastPageUrl(page);
        String nextPageUrl = constructNextPageUrl(page);
        String previousPageUrl = constructPreviousPageUrl(page);

        MultiValueMap<String, String> pageHeadersMap = new LinkedMultiValueMap<>();

        if (firstPageUrl != null)
            pageHeadersMap.add("first", firstPageUrl);

        if (lastPageUrl != null)
            pageHeadersMap.add("last", lastPageUrl);

        if (previousPageUrl != null)
            pageHeadersMap.add("prev", previousPageUrl);

        if (nextPageUrl != null)
            pageHeadersMap.add("next", nextPageUrl);

        return new HttpHeaders(pageHeadersMap);
    }
}
