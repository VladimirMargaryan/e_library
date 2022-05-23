package com.app.e_library.persistence;

import com.app.e_library.persistence.entity.BookGenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookGenreRepository extends JpaRepository<BookGenreEntity, Long> {

    BookGenreEntity getBookGenreEntityByName(String name);
}
