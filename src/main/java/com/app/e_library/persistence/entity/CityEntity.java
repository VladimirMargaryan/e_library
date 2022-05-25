package com.app.e_library.persistence.entity;


import lombok.*;

import javax.persistence.*;
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
        name = "city",
        uniqueConstraints = {
                @UniqueConstraint(name = "city_name_unique", columnNames = "city_name")
        }
)
public class CityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "city_name", nullable = false)
    @NotBlank
    private String name;

    @OneToMany(
            targetEntity = AddressEntity.class,
            mappedBy = "city",
            cascade=CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    @ToString.Exclude
    private List<AddressEntity> addresses;

    public CityEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public CityEntity(String city) {
        this.name = city;
    }

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
