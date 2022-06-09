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

    @JsonIgnore
    private final HttpHeaders pageHeaders;


    public PageResponse(Page<T> page) {
        this.content = page.getContent();
        this.totalPages = page.getTotalPages();
        this.pageHeaders = buildPageHeaders(page);
    }

    private String buildPageUrl(int pageNumber){
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replaceQueryParam("page", pageNumber)
                .build().toUriString();
    }


    private HttpHeaders buildPageHeaders(Page<T> page){
        MultiValueMap<String, String> pageHeadersMap = new LinkedMultiValueMap<>();

        if (!page.isFirst() && page.hasPrevious()) {
            String firstPageUrl = buildPageUrl(0);
            pageHeadersMap.add("first", firstPageUrl);
        }

        if (!page.isLast() && page.hasNext()) {
            String lastPageUrl = buildPageUrl(page.getTotalPages() - 1);
            pageHeadersMap.add("last", lastPageUrl);
        }

        if (page.hasPrevious()) {
            String previousPageUrl = buildPageUrl(page.getNumber() - 1);
            pageHeadersMap.add("prev", previousPageUrl);
        }

        if (page.hasNext()) {
            String nextPageUrl = buildPageUrl(page.getNumber() + 1);
            pageHeadersMap.add("next", nextPageUrl);
        }

        return new HttpHeaders(pageHeadersMap);
    }
}
