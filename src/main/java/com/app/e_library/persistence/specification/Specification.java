package com.app.e_library.persistence.specification;

import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.criteria.*;

@Builder
@AllArgsConstructor
public class Specification<T> implements org.springframework.data.jpa.domain.Specification<T> {

    private final SearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

            if (criteria.getOperation().equals(SearchOperation.GREATER_THAN)) {
                return builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());

            } else if (criteria.getOperation().equals(SearchOperation.LESS_THAN)) {
                return builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());

            } else if (criteria.getOperation().equals(SearchOperation.GREATER_THAN_EQUAL)) {
                return builder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());

            } else if (criteria.getOperation().equals(SearchOperation.LESS_THAN_EQUAL)) {
                return builder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());

            } else if (criteria.getOperation().equals(SearchOperation.NOT_EQUAL)) {
                return builder.notEqual(getExpression(root.get(criteria.getKey()), builder), criteria.getValue());

            } else if (criteria.getOperation().equals(SearchOperation.EQUAL)) {
                return builder.equal(getExpression(root.get(criteria.getKey()), builder), criteria.getValue());

            } else if (criteria.getOperation().equals(SearchOperation.MATCH)) {
                return builder.like(getExpression(root.get(criteria.getKey()), builder),
                        "%" + criteria.getValue().toString().toLowerCase() + "%");

            } else if (criteria.getOperation().equals(SearchOperation.MATCH_END)) {
                return builder.like(getExpression(root.get(criteria.getKey()), builder),
                        criteria.getValue().toString().toLowerCase() + "%");

            } else if (criteria.getOperation().equals(SearchOperation.MATCH_START)) {
                return builder.like(getExpression(root.get(criteria.getKey()), builder),
                        "%" + criteria.getValue().toString().toLowerCase());

            } else if (criteria.getOperation().equals(SearchOperation.IN)) {
                return builder.in(root.get(criteria.getKey())).value(criteria.getValue());

            } else
                return builder.not(root.get(criteria.getKey())).in(criteria.getValue());

    }

    private Expression<String> getExpression(Expression<?> expression, CriteriaBuilder builder){
        return builder.lower(builder.function("replace", String.class, expression, builder.literal(" "), builder.literal("")));
    }

}
