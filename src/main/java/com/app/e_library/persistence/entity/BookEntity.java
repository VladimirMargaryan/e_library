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

@Getter
@Setter
@ToString
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="genre_id", nullable=false)
    @ToString.Exclude
    private BookGenreEntity bookGenre;

    @Column(name = "book_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private BookStatusType bookStatus;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id", nullable = false)
    @NonNull
    @ToString.Exclude
    private PublisherEntity publisher;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "pick_detail_id")
    @ToString.Exclude
    private PickDetailEntity pickDetail;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    @NonNull
    @ToString.Exclude
    private AuthorEntity author;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "book_image_id")
    @ToString.Exclude
    private BookImageEntity bookImage;

    @OneToMany(
            targetEntity = ReceiptEntity.class,
            mappedBy = "book",
            cascade=CascadeType.ALL,
            fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<ReceiptEntity> receipts;


    public BookEntity(String isbn, String title, short publicationYear,
                      int pageCount,BookGenreEntity bookGenre,
                      BookStatusType bookStatus, @NonNull PublisherEntity publisher,
                      @NonNull AuthorEntity author) {

        this.isbn = isbn;
        this.title = title;
        this.publicationYear = publicationYear;
        this.pageCount = pageCount;
        this.bookGenre = bookGenre;
        this.bookStatus = bookStatus;
        this.publisher = publisher;
        this.author = author;
    }

    public BookEntity(String isbn, String title, short publicationYear,
                      int pageCount, BookGenreEntity bookGenre, BookStatusType bookStatus,
                      @NonNull PublisherEntity publisher, @NonNull AuthorEntity author,
                      BookImageEntity bookImage) {

        this.isbn = isbn;
        this.title = title;
        this.publicationYear = publicationYear;
        this.pageCount = pageCount;
        this.bookGenre = bookGenre;
        this.bookStatus = bookStatus;
        this.publisher = publisher;
        this.author = author;
        this.bookImage = bookImage;
    }

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
