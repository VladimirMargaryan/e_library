package com.app.e_library.persistence.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pick_detail")
public class PickDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "pickDetail", cascade = CascadeType.ALL)
    private BookEntity book;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pivked_by_user", nullable = false)
    @NonNull
    private UserEntity usedByUser;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "reserved_by_user", nullable = false)
    @NonNull
    private UserEntity reservedByUser;

    @Column(name = "reserved_until", nullable = false)
    @Valid
    private Long reservedUntil;

    @Column(name = "return_date", nullable = false)
    @Valid
    private Long returnDate;

    public PickDetailEntity(Long id, Long reservedUntil, Long returnDate) {
        this.id = id;
        this.reservedUntil = reservedUntil;
        this.returnDate = returnDate;
    }

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
