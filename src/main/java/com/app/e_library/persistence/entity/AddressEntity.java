package com.app.e_library.persistence.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(
        name = "address",
        indexes = {
                @Index(name = "street_name_idx", columnList = "street_name"),
                @Index(name = "street_number_idx", columnList = "street_number"),
                @Index(name = "street_idx", columnList = "street_name, street_number")
        }
)
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    @ToString.Exclude
    @NonNull
    private CityEntity city;

    @Column(name = "street_name", nullable = false)
    private String street;

    @Column(name = "street_number", nullable = false)
    @Valid
    private int streetNumber;

    @OneToOne(mappedBy = "address")
    @NonNull
    @ToString.Exclude
    private UserEntity user;

    public AddressEntity(@NonNull CityEntity city, String street, int streetNumber) {
        this.city = city;
        this.street = street;
        this.streetNumber = streetNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddressEntity)) return false;
        AddressEntity that = (AddressEntity) o;
        return streetNumber == that.streetNumber && Objects.equals(id, that.id)
                && Objects.equals(city, that.city) && Objects.equals(street, that.street)
                && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, city, street, streetNumber, user);
    }
}
