package com.app.e_library.persistence;

import com.app.e_library.persistence.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;

@Repository
public interface CityRepository extends JpaRepository<CityEntity, Long> {

    CityEntity getCityEntityByName(String name);
}
