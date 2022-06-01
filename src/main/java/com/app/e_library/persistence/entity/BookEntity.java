package com.app.e_library.persistence.entity;

import com.app.e_library.service.dto.BookStatusType;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "book",
        uniqueConstraints = {
                @UniqueConstraint(name = "book_isbn_unique", columnNames = "isbn")
        },
        indexes = {
                @Index(name = "title_index", columnList = "title"),
                @Index(name = "publication_year_index", columnList = "publication_year"),
        }
)
public class BookEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "isbn", nullable = false)
    @NotBlank
    private String isbn;

    @Column(name = "title", nullable = false)
    @NotBlank
    private String title;

    @Column(name = "publication_year")
    @Valid
    private short publicationYear;

    @Column(name = "page_count")
    @Valid
    @Range(min = 50, max = 5000)
    private int pageCount;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="genre_id", nullable=false)
    @ToString.Exclude
    private BookGenreEntity bookGenre;

    @Column(name = "book_status", nullable = false)
    @Enumerated(STRING)
    private BookStatusType bookStatus;

    @ManyToOne(cascade = ALL, fetch = LAZY)
    @JoinColumn(name = "publisher_id", nullable = false)
    @NonNull
    @ToString.Exclude
    private PublisherEntity publisher;

    @OneToOne(cascade = ALL, fetch = LAZY, orphanRemoval = true)
    @JoinColumn(name = "pick_detail_id")
    @ToString.Exclude
    private PickDetailEntity pickDetail;

    @ManyToOne(cascade = ALL, fetch = LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    @NonNull
    @ToString.Exclude
    private AuthorEntity author;

    @OneToOne(cascade = ALL, fetch = LAZY, orphanRemoval = true)
    @JoinColumn(name = "book_image_id")
    @ToString.Exclude
    private BookImageEntity bookImage;

    @OneToMany(
            mappedBy = "book",
            cascade = ALL,
            fetch = LAZY,
            orphanRemoval = true)
    @ToString.Exclude
    private List<ReceiptEntity> receipts;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BookEntity that = (BookEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
