package com.app.e_library.persistence.specification;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SearchCriteria {

    private String key;
    private Object value;
    private SearchOperation operation;

    public SearchCriteria(String key, Object value, SearchOperation operation) {
        this.key = key;

        if (value instanceof String)
            this.value = value.toString().replaceAll("\\s+", "").toLowerCase();
        else
            this.value = value;

        this.operation = operation;
    }
}
