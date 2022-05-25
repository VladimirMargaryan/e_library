package com.app.e_library.persistence.entity;

import com.app.e_library.service.dto.UserStatusType;
import com.app.e_library.validation.ValidEmail;
import com.app.e_library.validation.ValidPassword;
import com.app.e_library.validation.ValidPhone;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    @NotBlank
    private String firstname;

    @Column(name = "last_name", nullable = false)
    @NotBlank
    private String lastname;

    @Column(name = "ssn", nullable = false)
    @NotBlank
    private String ssn;

    @Column(name = "email", nullable = false)
    @NotBlank
    @ValidEmail
    private String email;

    @Column(name = "password", nullable = false)
    @NotBlank
    @ValidPassword
    private String password;

    @Column(name = "register_date", nullable = false)
    @Valid
    private Long registration_date;

    @Column(name = "phone", nullable = false)
    @NotBlank
    @ValidPhone
    private String phone;

    @Transient
    @Size(min = 24, max = 24)
    private String resetPasswordToken;

    @Transient
    @Valid
    private Long resetPasswordTokenCreationDate;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "address_id", nullable = false)
    @NonNull
    @ToString.Exclude
    private AddressEntity address;

    @Column(name = "user_status", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotBlank
    private UserStatusType userStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    @NonNull
    @ToString.Exclude
    private RoleEntity role;

    @OneToMany(
            targetEntity = PickDetailEntity.class,
            mappedBy = "usedByUser",
            cascade=CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    @ToString.Exclude
    private List<PickDetailEntity> pickedBookDetails;

    @OneToMany(
            targetEntity = PickDetailEntity.class,
            mappedBy = "reservedByUser",
            cascade=CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    @ToString.Exclude
    private List<PickDetailEntity> reservedBookDetails;

    @OneToMany(
            targetEntity = ReceiptEntity.class,
            mappedBy = "user",
            cascade=CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    @ToString.Exclude
    private List<ReceiptEntity> receipts;


    public UserEntity(Long id, String firstname, String lastname, String ssn,
                      String email, String password, Long registration_date, String phone,
                      String resetPasswordToken, Long resetPasswordTokenCreationDate,
                      AddressEntity address, UserStatusType userStatus, RoleEntity role) {

        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.ssn = ssn;
        this.email = email;
        this.password = password;
        this.registration_date = registration_date;
        this.phone = phone;
        this.resetPasswordToken = resetPasswordToken;
        this.resetPasswordTokenCreationDate = resetPasswordTokenCreationDate;
        this.address = address;
        this.userStatus = userStatus;
        this.role = role;
    }

    public UserEntity(String firstname, String lastname, String ssn,
                      String email, String password, Long registration_date,
                      String phone, @NonNull AddressEntity address, UserStatusType
                              userStatus, @NonNull RoleEntity role) {

        this.firstname = firstname;
        this.lastname = lastname;
        this.ssn = ssn;
        this.email = email;
        this.password = password;
        this.registration_date = registration_date;
        this.phone = phone;
        this.address = address;
        this.userStatus = userStatus;
        this.role = role;
    }

    public UserEntity(String firstname, String lastname, String ssn,
                      String email, String password, Long registration_date,
                      String phone, UserStatusType userStatus) {

        this.firstname = firstname;
        this.lastname = lastname;
        this.ssn = ssn;
        this.email = email;
        this.password = password;
        this.registration_date = registration_date;
        this.phone = phone;
        this.userStatus = userStatus;
    }

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
