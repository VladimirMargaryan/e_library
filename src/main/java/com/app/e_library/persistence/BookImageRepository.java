package com.app.e_library.persistence;

import com.app.e_library.persistence.entity.BookImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookImageRepository extends JpaRepository<BookImageEntity, Long> {
}
