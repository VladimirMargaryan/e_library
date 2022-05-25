package com.app.e_library.persistence.specification;

import com.app.e_library.persistence.entity.AddressEntity;
import com.app.e_library.persistence.entity.CityEntity;
import com.app.e_library.persistence.entity.UserEntity;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Setter
@NoArgsConstructor
public class UserSearchSpecification implements Specification<UserEntity> {

    private String filterBy;
    private String searchKey;


    public UserSearchSpecification(String searchKey, String filterBy) {
        this.searchKey = searchKey.replaceAll("\\s+", "").toLowerCase();
        this.filterBy = filterBy;
    }


    @Override
    public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        if (Objects.equals(filterBy,"address"))
            return builder.or(searchByAddress(root, builder, searchKey));
        else
            return builder.or(searchByKeyword(root, builder, searchKey));
    }


    private Expression<String> getExpression(Expression<?> expression, CriteriaBuilder builder){
        return builder.lower(builder.function("replace", String.class, expression, builder.literal(" "), builder.literal("")));
    }

    private Predicate[] searchByKeyword(Root<UserEntity> root, CriteriaBuilder builder, String searchKey) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.like(getExpression(root.get("firstname"), builder), searchKey + "%"));
        predicates.add(builder.like(getExpression(root.get("lastname"), builder), searchKey + "%"));
        predicates.add(builder.like(getExpression(
                builder.concat(root.get("firstname"), root.get("lastname")), builder), searchKey + "%"));
        predicates.add(builder.equal(getExpression(root.get("ssn"), builder), searchKey));
        predicates.add(builder.equal(getExpression(root.get("email"), builder), searchKey));
        predicates.add(builder.equal(getExpression(root.get("phone"), builder), searchKey));
        return predicates.toArray(new Predicate[0]);
    }

    private Predicate[] searchByAddress(Root<UserEntity> root, CriteriaBuilder builder, String searchKey){
        List<Predicate> predicates = new ArrayList<>();
        Join<UserEntity, AddressEntity> addressEntityJoin = root.join("address", JoinType.INNER);
        Join<UserEntity, CityEntity> cityEntityJoin = addressEntityJoin.join("city", JoinType.INNER);
        predicates.add(builder.like(getExpression(cityEntityJoin.get("name"), builder), searchKey + "%"));
        predicates.add(builder.like(getExpression(addressEntityJoin.get("street"), builder), searchKey + "%"));
        predicates.add(builder.like(getExpression(addressEntityJoin.get("streetNumber"), builder), searchKey + "%"));

        predicates.add(builder.like(
                getExpression(
                        builder.concat(addressEntityJoin.get("street"), addressEntityJoin.get("streetNumber")), builder),
                searchKey + "%"));

        predicates.add(builder.like(
                getExpression(
                        builder.concat(
                                cityEntityJoin.get("name"),
                                builder.concat(addressEntityJoin.get("street"), addressEntityJoin.get("streetNumber"))
                        ), builder),
                searchKey + "%"));

        return predicates.toArray(new Predicate[0]);
    }
}
