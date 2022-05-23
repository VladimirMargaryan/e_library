package com.app.e_library.service.dto;

import com.app.e_library.persistence.entity.BookGenreEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookGenreDto {

    private Long id;
    private String name;

    public BookGenreDto(String name) {
        this.name = name;
    }

    public static BookGenreDto mapToDto(BookGenreEntity bookGenreEntity){
        BookGenreDto bookGenreDto = new BookGenreDto();
        bookGenreDto.setId(bookGenreEntity.getId());
        bookGenreDto.setName(bookGenreEntity.getName());

        return bookGenreDto;
    }

    public static BookGenreEntity mapToEntity(BookGenreDto bookGenreDto){
        BookGenreEntity bookGenreEntity = new BookGenreEntity();
        bookGenreEntity.setId(bookGenreDto.getId());
        bookGenreEntity.setName(bookGenreDto.getName());

        return bookGenreEntity;
    }

    public static List<BookGenreDto> mapToDtoList(List<BookGenreEntity> bookGenreEntities) {
        return bookGenreEntities.stream().map(BookGenreDto::mapToDto).collect(Collectors.toList());
    }

    public static List<BookGenreEntity> mapToEntityList(List<BookGenreDto> bookGenreDtos) {
        return bookGenreDtos.stream().map(BookGenreDto::mapToEntity).collect(Collectors.toList());
    }

}
