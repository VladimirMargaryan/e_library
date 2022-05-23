package com.app.e_library.persistence;

import com.app.e_library.persistence.entity.UserEntity;
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

    @Query("select distinct user from UserEntity user " +
            "inner join AddressEntity address on user.address.id = address.id " +
            "inner join CityEntity city on address.city.id = city.id " +
            "where lower(user.firstname) like concat(:key, '%') or " +
            "lower(user.lastname) like concat(:key, '%') or " +
            "lower(user.ssn) = :key or " +
            "lower(user.email) = :key or " +
            "lower(user.phone) = :key or " +
            "lower(concat(address.street, ' ', address.streetNumber)) = :key or " +
            "lower(address.street) like concat(:key, '%') or " +
            "lower(city.name) like concat(:key, '%')")
    Page<UserEntity> searchByKeyword(Pageable pageable, @Param("key") String keyword);

}
