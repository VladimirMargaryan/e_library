package com.app.e_library.persistence.pagination;

import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Getter
public class PageResponse<T> {

    private final Page<T> page;
    private final String nextPageUrl;
    private final String previousPageUrl;
    private final String firstPageUrl;
    private final String lastPageUrl;

    public PageResponse(Page<T> page) {
        this.page = page;
        this.firstPageUrl = constructFirstPageUrl();
        this.lastPageUrl = constructLastPageUrl();
        this.nextPageUrl = constructNextPageUrl();
        this.previousPageUrl = constructPreviousPageUrl();
    }

    private String constructNextPageUrl(){
        return page.hasNext() ? ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replaceQueryParam("page", page.getNumber() + 1)
                .build().toUriString() : null;
    }

    private String constructPreviousPageUrl(){
        return page.hasPrevious() ? ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replaceQueryParam("page", page.getNumber() - 1)
                .build().toUriString() : null;
    }

    private String constructFirstPageUrl(){
        return !page.isFirst() && page.hasPrevious() ? ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replaceQueryParam("page", 0)
                .build().toUriString() : null;
    }

    private String constructLastPageUrl(){
        return !page.isLast() && page.hasNext() ? ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replaceQueryParam("page", page.getTotalPages() - 1)
                .build().toUriString() : null;
    }

    public MultiValueMap<String, String> buildHttpHeadersForPages(){
        LinkedMultiValueMap<String, String> pageMap = new LinkedMultiValueMap<>();

        if (firstPageUrl != null)
            pageMap.add("first", firstPageUrl);

        if (lastPageUrl != null)
            pageMap.add("last", lastPageUrl);

        if (previousPageUrl != null)
            pageMap.add("prev", previousPageUrl);

        if (nextPageUrl != null)
            pageMap.add("next", nextPageUrl);

        return pageMap;
    }
}
