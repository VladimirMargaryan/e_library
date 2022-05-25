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
            "where lower(user.firstname) like concat(:key, '%') or " +
            "lower(user.lastname) like concat(:key, '%') or " +
            "lower(user.ssn) = :key or " +
            "lower(user.email) = :key or " +
            "lower(user.phone) = :key ")
    Page<UserEntity> searchByKeyword(Pageable pageable, @Param("key") String keyword);

    @Query("select distinct user from UserEntity user " +
            "inner join AddressEntity address on user.address.id = address.id " +
            "where lower(address.street) like concat(:address, '%') or " +
            "lower(concat(address.street, ' ', address.streetNumber)) = :address")
    Page<UserEntity> searchByAddress(Pageable pageable, @Param("address") String address);


    @Query("select distinct user from UserEntity user " +
            "inner join CityEntity city on user.address.city.id = city.id " +
            "where lower(city.name) like concat(:city, '%')")
    Page<UserEntity> searchByCity(Pageable pageable, @Param("city") String city);

}
