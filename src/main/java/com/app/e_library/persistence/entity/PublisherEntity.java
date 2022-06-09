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
        name = "publisher",
        uniqueConstraints = {
                @UniqueConstraint(name = "publisher_name_unique", columnNames = "publisher_name")
        }
)
public class PublisherEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "publisher_name", nullable = false)
    @NotBlank
    private String publisherName;

    @OneToMany(mappedBy = "publisher")
    @ToString.Exclude
    private List<BookEntity> books;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PublisherEntity)) return false;
        PublisherEntity that = (PublisherEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(publisherName, that.publisherName)
                && Objects.equals(books, that.books);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, publisherName, books);
    }
}
