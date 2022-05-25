package com.app.e_library.service.dto;

import com.app.e_library.persistence.entity.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
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


    public static BookDto mapToDto(BookEntity bookEntity){
        BookDto bookDto = new BookDto();
        bookDto.setId(bookEntity.getId());
        bookDto.setIsbn(bookEntity.getIsbn());
        bookDto.setTitle(bookEntity.getTitle());
        bookDto.setPublicationYear(bookEntity.getPublicationYear());
        bookDto.setPageCount(bookEntity.getPageCount());
        bookDto.setBookGenre(BookGenreDto.mapToDto(bookEntity.getBookGenre()));
        bookDto.setBookStatus(bookEntity.getBookStatus());
        if (bookEntity.getPickDetail() != null)
            bookDto.setPickDetail(PickDetailDto.mapToDto(bookEntity.getPickDetail()));
        bookDto.setPublisher(PublisherDto.mapToDto(bookEntity.getPublisher()));
        bookDto.setAuthor(AuthorDto.mapToDto(bookEntity.getAuthor()));
        bookDto.setImageDto(BookImageDto.mapToDto(bookEntity.getBookImage()));
        return bookDto;
    }

    public static BookEntity mapToEntity(BookDto bookDto){
        BookEntity bookEntity = new BookEntity();
        bookEntity.setId(bookDto.getId());
        bookEntity.setIsbn(bookDto.getIsbn());
        bookEntity.setTitle(bookDto.getTitle());
        bookEntity.setPublicationYear(bookDto.getPublicationYear());
        bookEntity.setPageCount(bookDto.getPageCount());
        bookEntity.setBookStatus(bookDto.getBookStatus());

        if (bookDto.getBookGenre() != null)
            bookEntity.setBookGenre(BookGenreDto.mapToEntity(bookDto.getBookGenre()));

        if (bookDto.getPublisher() != null)
            bookEntity.setPublisher(PublisherDto.mapToEntity(bookDto.getPublisher()));

        if (bookDto.getAuthor() != null)
            bookEntity.setAuthor(AuthorDto.mapToEntity(bookDto.getAuthor()));

        if (bookDto.getPickDetail() != null)
            bookEntity.setPickDetail(PickDetailDto.mapToEntity(bookDto.getPickDetail()));

        if (bookDto.getImageDto() != null)
            bookEntity.setBookImage(BookImageDto.mapToEntity(bookDto.getImageDto()));

        return bookEntity;
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
