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
        name = "user_role",
        uniqueConstraints = {
                @UniqueConstraint(name = "role_name_unique", columnNames = "name")
        },
        indexes = {
                @Index(name = "name_idx", columnList = "name")
        }
)
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @NotBlank
    private String rollName;

    @OneToMany(
            targetEntity = UserEntity.class,
            mappedBy = "role",
            cascade=CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    @ToString.Exclude
    private List<UserEntity> users;

    public RoleEntity(Long id, String rollName) {
        this.id = id;
        this.rollName = rollName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoleEntity)) return false;
        RoleEntity that = (RoleEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(rollName, that.rollName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rollName);
    }
}
