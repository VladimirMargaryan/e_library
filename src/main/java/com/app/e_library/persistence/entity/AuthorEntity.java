package com.app.e_library.persistence.entity;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "author",
        uniqueConstraints = {
                @UniqueConstraint(name = "author_name_unique", columnNames = "author_name")
        }
)
public class AuthorEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "author_name", nullable = false)
    @NotBlank
    private String name;

    @OneToMany(mappedBy = "author")
    @ToString.Exclude
    private List<BookEntity> books;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthorEntity)) return false;
        AuthorEntity that = (AuthorEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name)
                && Objects.equals(books, that.books);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, books);
    }
}
