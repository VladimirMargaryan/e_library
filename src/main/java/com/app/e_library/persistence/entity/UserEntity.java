package com.app.e_library.persistence.entity;

import com.app.e_library.service.dto.UserStatusType;
import com.app.e_library.validation.ValidEmail;
import com.app.e_library.validation.ValidPassword;
import com.app.e_library.validation.ValidPhone;
import com.app.e_library.validation.ValidSsn;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

import static javax.persistence.CascadeType.*;
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
        name = "user",
        uniqueConstraints = {
                @UniqueConstraint(name = "user_ssn_unique", columnNames = "ssn"),
                @UniqueConstraint(name = "user_email_unique", columnNames = "email"),
                @UniqueConstraint(name = "user_phone_unique", columnNames = "phone")
        }
)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    @NotBlank
    @NotNull
    private String firstname;

    @Column(name = "last_name", nullable = false)
    @NotBlank
    @NotNull
    private String lastname;

    @Column(name = "ssn", nullable = false)
    @ValidSsn
    private String ssn;

    @Column(name = "email", nullable = false)
    @ValidEmail
    private String email;

    @Column(name = "password", nullable = false)
    @ValidPassword
    private String password;

    @Column(name = "register_date", nullable = false)
    @Valid
    private Long registrationDate;

    @Column(name = "phone", nullable = false)
    @ValidPhone
    private String phone;

    @Transient
    @Size(min = 24, max = 24)
    private String resetPasswordToken;

    @Transient
    @Valid
    private Long resetPasswordTokenCreationDate;

    @OneToOne(cascade = ALL, fetch = LAZY, orphanRemoval = true)
    @JoinColumn(name = "address_id", nullable = false)
    @NonNull
    @ToString.Exclude
    private AddressEntity address;

    @Column(name = "user_status", nullable = false)
    @Enumerated(STRING)
    @NotBlank
    private UserStatusType userStatus;

    @ManyToOne(cascade = {PERSIST, DETACH, REFRESH, MERGE},
            fetch = LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    @NonNull
    @ToString.Exclude
    private RoleEntity role;

    @OneToMany(
            mappedBy = "usedByUser",
            cascade = ALL,
            fetch = LAZY,
            orphanRemoval = true)
    @ToString.Exclude
    private List<PickDetailEntity> pickedBookDetails;

    @OneToMany(
            mappedBy = "reservedByUser",
            cascade = ALL,
            fetch = LAZY,
            orphanRemoval = true)
    @ToString.Exclude
    private List<PickDetailEntity> reservedBookDetails;

    @OneToMany(
            mappedBy = "user",
            cascade = ALL,
            fetch = LAZY,
            orphanRemoval = true)
    @ToString.Exclude
    private List<ReceiptEntity> receipts;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserEntity that = (UserEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
