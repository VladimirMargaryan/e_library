package com.app.e_library.persistence;

import com.app.e_library.persistence.entity.BookEntity;
import com.app.e_library.service.dto.BookDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long>, JpaSpecificationExecutor<BookEntity> {

    Optional<BookEntity> findBookEntityByIsbn(String isbn);

    @Query("from BookEntity book " +
            "inner join book.bookImage image " +
            "where image.imageDownloadStatus='TODO'")
    List<BookEntity> getFirstNBooksByImageDownloadStatus(Pageable pageable);

    @Query("from BookEntity book " +
            "inner join  book.bookImage image " +
            "where image.imageDownloadStatus='IN_PROGRESS'" +
            " and :currentTime > (image.imageDownloadStartTime + 60000)")
    List<BookEntity> getImageDownloadFailedBooks(@Param("currentTime") Long currentTImeInMillis, Pageable pageable);


    @Query("select new com.app.e_library.service.dto.BookDto(b.id, b.isbn, b.title, " +
            "b.publicationYear, b.pageCount, b.bookStatus, i.imageURLLarge, i.imageURLSmall, g.name, " +
            "a.name, p.publisherName) from BookEntity b " +
            "inner join b.bookGenre g " +
            "inner join b.publisher p " +
            "inner join b.author a " +
            "inner join b.bookImage i where " +
            "(:isbn = '' or (lower(function('replace', b.isbn, ' ', '')) like concat(:isbn, '%'))) and " +
            "(:title = '' or (lower(function('replace', b.title, ' ', '')) like concat(:title, '%'))) and " +
            "(:publicationYear = 0 or b.publicationYear = :publicationYear) and " +
            "(:genre = '' or (lower(function('replace', g.name, ' ', '')) like concat(:genre, '%'))) and " +
            "(:author = '' or (lower(function('replace', a.name, ' ', '')) like concat(:author, '%'))) and " +
            "(:publisher = '' or (lower(function('replace', p.publisherName, ' ', '')) like concat(:publisher, '%')))")
    Page<BookDto> searchBook(@Param("isbn") String isbn,
                             @Param("title") String title,
                             @Param("publicationYear") int publicationYear,
                             @Param("genre") String genre,
                             @Param("author") String author,
                             @Param("publisher") String publisher,
                             Pageable pageable);

}
