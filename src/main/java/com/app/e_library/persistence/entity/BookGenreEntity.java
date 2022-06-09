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
        name = "book_genre",
        uniqueConstraints = {
                @UniqueConstraint(name = "book_genre_name_unique", columnNames = "genre_name")
        }
)
public class BookGenreEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "genre_name", nullable = false)
    @NotBlank
    private String name;

    @OneToMany(mappedBy = "bookGenre")
    @ToString.Exclude
    private List<BookEntity> books;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookGenreEntity)) return false;
        BookGenreEntity that = (BookGenreEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(books, that.books);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, books);
    }
}
