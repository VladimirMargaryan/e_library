package com.app.e_library.service.dto;

import com.app.e_library.persistence.entity.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookDto {

    private Long id;
    private String isbn;
    private String title;
    private short publicationYear;
    private int pageCount;
    private BookImageDto imageDto;
    private BookGenreDto bookGenre;
    private BookStatusType bookStatus;
    private PublisherDto publisher;
    private PickDetailDto pickDetail;
    private AuthorDto author;

    public BookDto(Long id, String isbn, String title, short publicationYear,
                   int pageCount, BookStatusType bookStatus, String imageUrlLarge,
                   String imageUrlSmall, String genre, String author, String publisher) {

        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.publicationYear = publicationYear;
        this.pageCount = pageCount;
        this.bookStatus = bookStatus;

        this.imageDto = BookImageDto
                .builder()
                .imageURLSmall(imageUrlSmall)
                .imageURLLarge(imageUrlLarge)
                .build();

        this.bookGenre = BookGenreDto
                .builder()
                .name(genre)
                .build();

        this.author = AuthorDto
                .builder()
                .name(author)
                .build();

        this.publisher = PublisherDto
                .builder()
                .publisherName(publisher)
                .build();
    }

    public static BookDto mapToDto(BookEntity bookEntity){

        return BookDto
                .builder()
                .id(bookEntity.getId())
                .isbn(bookEntity.getIsbn())
                .title(bookEntity.getTitle())
                .publicationYear(bookEntity.getPublicationYear())
                .pageCount(bookEntity.getPageCount())
                .bookGenre(BookGenreDto.mapToDto(bookEntity.getBookGenre()))
                .bookStatus(bookEntity.getBookStatus())
                .pickDetail(bookEntity.getPickDetail() == null ? PickDetailDto
                        .builder()
                        .build() : PickDetailDto.mapToDto(bookEntity.getPickDetail()))
                .publisher(PublisherDto.mapToDto(bookEntity.getPublisher()))
                .author(AuthorDto.mapToDto(bookEntity.getAuthor()))
                .imageDto(BookImageDto.mapToDto(bookEntity.getBookImage()))
                .build();
    }

    public static BookEntity mapToEntity(BookDto bookDto){

        return BookEntity
                .builder()
                .id(bookDto.getId())
                .isbn(bookDto.getIsbn())
                .title(bookDto.getTitle())
                .publicationYear(bookDto.getPublicationYear())
                .pageCount(bookDto.getPageCount())
                .bookStatus(bookDto.getBookStatus())
                .bookGenre(bookDto.getBookGenre() == null ? BookGenreEntity
                        .builder()
                        .build() : BookGenreDto.mapToEntity(bookDto.getBookGenre()))
                .pickDetail(bookDto.getPickDetail() == null ? PickDetailEntity
                        .builder()
                        .build() : PickDetailDto.mapToEntity(bookDto.getPickDetail()))
                .publisher(bookDto.getPublisher() == null ? PublisherEntity
                        .builder()
                        .build() : PublisherDto.mapToEntity(bookDto.getPublisher()))
                .author(bookDto.getAuthor() == null ? AuthorEntity
                        .builder()
                        .build() : AuthorDto.mapToEntity(bookDto.getAuthor()))
                .bookImage(bookDto.getImageDto() == null ? BookImageEntity
                        .builder()
                        .build() : BookImageDto.mapToEntity(bookDto.getImageDto()))
                .build();
    }

    public static List<BookDto> mapToDtoList(List<BookEntity> bookEntities){
        return bookEntities.stream().map(BookDto::mapToDto).collect(Collectors.toList());
    }

    public static Page<BookDto> mapToDtoPage(Page<BookEntity> bookEntities){
        return bookEntities.map(BookDto::mapToDto);
    }

    public static List<BookEntity> mapToEntityList(List<BookDto> bookDtos){
        return bookDtos.stream().map(BookDto::mapToEntity).collect(Collectors.toList());
    }
}
