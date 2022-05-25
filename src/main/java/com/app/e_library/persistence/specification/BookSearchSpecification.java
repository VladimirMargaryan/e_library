package com.app.e_library.persistence.specification;

import com.app.e_library.persistence.entity.*;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Setter
public class BookSearchSpecification implements Specification<BookEntity> {

    private String filterBy;
    private String searchKey;

    public BookSearchSpecification(String searchKey, String filterBy) {
        this.searchKey = searchKey.replaceAll("\\s+", "").toLowerCase();
        this.filterBy = filterBy;
    }


    @Override
    public Predicate toPredicate(Root<BookEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (Objects.equals(filterBy,"genre"))
            return builder.or(searchByGenre(root, builder, searchKey));
        else if (Objects.equals(filterBy,"author"))
            return builder.or(searchByAuthor(root, builder, searchKey));
        else if (Objects.equals(filterBy,"publisher"))
            return builder.or(searchByPublisher(root, builder, searchKey));
        else
            return builder.or(searchByKeyword(root, builder, searchKey));
    }

    private Expression<String> getExpression(Expression<?> expression, CriteriaBuilder builder){
        return builder.lower(builder.function("replace", String.class, expression, builder.literal(" "), builder.literal("")));
    }

    private Predicate[] searchByKeyword(Root<BookEntity> root, CriteriaBuilder builder, String searchKey) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(getExpression(root.get("publicationYear"), builder), searchKey));
        predicates.add(builder.equal(getExpression(root.get("isbn"), builder), searchKey));
        predicates.add(builder.like(getExpression(root.get("title"), builder), searchKey + "%"));
        return predicates.toArray(new Predicate[0]);
    }

    private Predicate searchByGenre(Root<BookEntity> root, CriteriaBuilder builder, String searchKey) {
        Join<BookEntity, BookGenreEntity> bookGenreEntityJoin = root.join("bookGenre", JoinType.INNER);
        return builder.like(getExpression(bookGenreEntityJoin.get("name"), builder), searchKey + "%");
    }

    private Predicate searchByAuthor(Root<BookEntity> root, CriteriaBuilder builder, String searchKey) {
        Join<BookEntity, AuthorEntity> authorEntityJoin = root.join("author", JoinType.INNER);
        return builder.like(getExpression(authorEntityJoin.get("name"), builder), searchKey + "%");
    }

    private Predicate searchByPublisher(Root<BookEntity> root, CriteriaBuilder builder, String searchKey) {
        Join<BookEntity, PublisherEntity> publisherEntityJoin = root.join("publisher", JoinType.INNER);
        return builder.like(getExpression(publisherEntityJoin.get("publisherName"), builder), searchKey + "%");
    }


}
