package com.app.e_library.persistence;

import com.app.e_library.persistence.entity.PickDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PickDetailRepository extends JpaRepository<PickDetailEntity, Long> {
}
