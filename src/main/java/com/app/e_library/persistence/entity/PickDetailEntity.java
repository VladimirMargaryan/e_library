package com.app.e_library.persistence.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.Objects;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pick_detail")
public class PickDetailEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @OneToOne(
            mappedBy = "pickDetail",
            cascade = ALL,
            fetch = LAZY)
    @ToString.Exclude
    private BookEntity book;

    @ManyToOne(cascade = ALL, fetch = LAZY)
    @JoinColumn(name = "pivked_by_user", nullable = false)
    @NonNull
    @ToString.Exclude
    private UserEntity usedByUser;

    @ManyToOne(cascade = ALL, fetch = LAZY)
    @JoinColumn(name = "reserved_by_user", nullable = false)
    @NonNull
    @ToString.Exclude
    private UserEntity reservedByUser;

    @Column(name = "reserved_until", nullable = false)
    @Valid
    private Long reservedUntil;

    @Column(name = "return_date", nullable = false)
    @Valid
    private Long returnDate;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PickDetailEntity)) return false;
        PickDetailEntity that = (PickDetailEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(book, that.book)
                && Objects.equals(usedByUser, that.usedByUser)
                && Objects.equals(reservedByUser, that.reservedByUser)
                && Objects.equals(reservedUntil, that.reservedUntil)
                && Objects.equals(returnDate, that.returnDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, book, usedByUser, reservedByUser, reservedUntil, returnDate);
    }
}
