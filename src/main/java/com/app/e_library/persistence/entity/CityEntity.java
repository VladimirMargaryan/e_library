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
        name = "city",
        uniqueConstraints = {
                @UniqueConstraint(name = "city_name_unique", columnNames = "city_name")
        }
)
public class CityEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "city_name", nullable = false)
    @NotBlank
    private String name;

    @OneToMany(mappedBy = "city")
    @ToString.Exclude
    private List<AddressEntity> addresses;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CityEntity)) return false;
        CityEntity that = (CityEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
