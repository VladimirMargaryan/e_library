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


    private HttpHeaders buildPageHeaders(Page<T> page){
        MultiValueMap<String, String> pageHeadersMap = new LinkedMultiValueMap<>();

        if (!page.isFirst() && page.hasPrevious()) {
            String firstPageUrl = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .replaceQueryParam("page", 0)
                    .build().toUriString();

            pageHeadersMap.add("first", firstPageUrl);
        }

        if (!page.isLast() && page.hasNext()) {
            String lastPageUrl = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .replaceQueryParam("page", page.getTotalPages() - 1)
                    .build().toUriString();

            pageHeadersMap.add("last", lastPageUrl);
        }

        if (page.hasPrevious()) {
            String previousPageUrl = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .replaceQueryParam("page", page.getNumber() - 1)
                    .build().toUriString();

            pageHeadersMap.add("prev", previousPageUrl);
        }

        if (page.hasNext()) {
            String nextPageUrl = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .replaceQueryParam("page", page.getNumber() + 1)
                    .build().toUriString();

            pageHeadersMap.add("next", nextPageUrl);
        }

        return new HttpHeaders(pageHeadersMap);
    }
}
