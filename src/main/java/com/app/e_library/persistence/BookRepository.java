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
            "inner join BookImageEntity image on image.id = book.bookImage.id " +
            "where image.imageDownloadStatus='TODO'")
    List<BookEntity> getFirstNBooksByImageDownloadStatus(Pageable pageable);

    @Query("from BookEntity book " +
            "inner join  book.bookImage image " +
            "where image.imageDownloadStatus='IN_PROGRESS'" +
            " and :currentTime > (image.imageDownloadStartTime + 60000)")
    List<BookEntity> getImageDownloadFailedBooks(@Param("currentTime") Long currentTImeInMillis, Pageable pageable);


    @Query("select distinct book from BookEntity book " +
            "inner join BookGenreEntity genre on book.bookGenre.id = genre.id " +
            "inner join PublisherEntity publisher on book.publisher.id = publisher.id " +
            "inner join AuthorEntity author on book.author.id = author.id " +
            "where lower(genre.name) like concat(:key, '%') or " +
            "lower(publisher.publisherName) like concat(:key, '%') or " +
            "lower(author.name) like concat(:key, '%') or " +
            "lower(book.title) like concat(:key, '%') or " +
            "lower(book.isbn) = :key or " +
            "book.publicationYear = :key")
    Page<BookEntity> searchByKeyword(Pageable pageable, @Param("key") String keyword);


    @Query("select distinct book from BookEntity book " +
            "inner join BookGenreEntity genre on book.bookGenre.id = genre.id " +
            "where lower(genre.name) like concat(:genre, '%') ")
    Page<BookEntity> searchByGenre(Pageable pageable, @Param("genre") String genre);


    @Query("select distinct book from BookEntity book " +
            "inner join AuthorEntity author on book.author.id = author.id " +
            "where lower(author.name) like concat(:author, '%')")
    Page<BookEntity> searchByAuthor(Pageable pageable, @Param("author") String author);

    @Query("select distinct book from BookEntity book " +
            "inner join PublisherEntity publisher on book.publisher.id = publisher.id " +
            "where lower(publisher.publisherName) like concat(:publisher, '%')")
    Page<BookEntity> searchByPublisher(Pageable pageable, @Param("publisher") String publisher);

}
