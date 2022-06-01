package com.app.e_library.persistence.entity;


import lombok.*;

import javax.persistence.*;
import java.util.Objects;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "receipt")
public class ReceiptEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "order_date", nullable = false)
    private Long orderDate;

    @Column(name = "expiration_date", nullable = false)
    private Long expirationDate;

    @ManyToOne(cascade = ALL, fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NonNull
    @ToString.Exclude
    private UserEntity user;

    @ManyToOne(cascade = ALL, fetch = LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    @NonNull
    @ToString.Exclude
    private BookEntity book;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReceiptEntity)) return false;
        ReceiptEntity that = (ReceiptEntity) o;
        return Objects.equals(id, that.id)
                && Objects.equals(orderDate, that.orderDate)
                && Objects.equals(expirationDate, that.expirationDate)
                && Objects.equals(user, that.user)
                && Objects.equals(book, that.book);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderDate, expirationDate, user, book);
    }
}
