package com.app.e_library.persistence;

import com.app.e_library.persistence.entity.UserEntity;
import com.app.e_library.service.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {

    UserEntity getUserEntityByEmail(String email);

    @Query("select new com.app.e_library.service.dto.UserDto(user.id, user.firstname," +
            " user.lastname, user.ssn, user.email, user.phone, user.registrationDate," +
            " address.street, address.streetNumber, city.name, user.userStatus) from UserEntity user " +
            "inner join user.address address " +
            "inner join user.address.city city " +
            "where (:firstname = '' or (lower(function('replace', user.firstname, ' ', '')) like concat(:firstname, '%'))) and " +
            "(:lastname = '' or (lower(function('replace', user.lastname, ' ', '')) like concat(:lastname, '%'))) and " +
            "(:ssn = '' or (lower(function('replace', user.ssn, '-', '')) = :ssn)) and " +
            "(:email = '' or (lower(function('replace', user.email, ' ', '')) = :email)) and " +
            "(:phone = '' or (lower(function('replace', user.phone, '-', '')) = :phone)) and " +
            "(:street = '' or (lower(function('replace', address.street, ' ', '')) like concat(:street, '%'))) and " +
            "(:streetNumber = 0 or address.streetNumber = :streetNumber) and " +
            "(:city = '' or (lower(function('replace', city.name, ' ', '')) like concat(:city, '%')))")
    Page<UserDto> searchUser(@Param("firstname") String firstname,
                             @Param("lastname") String lastname,
                             @Param("ssn") String ssn,
                             @Param("email") String email,
                             @Param("phone") String phone,
                             @Param("street") String street,
                             @Param("streetNumber") Integer streetNumber,
                             @Param("city") String city,
                             Pageable pageable
    );

}
