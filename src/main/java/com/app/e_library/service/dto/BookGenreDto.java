package com.app.e_library.service.dto;

import com.app.e_library.persistence.entity.BookGenreEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@JsonInclude(NON_NULL)
public class BookGenreDto {

    private Long id;
    private String name;


    public static BookGenreDto mapToDto(BookGenreEntity bookGenreEntity){

        return BookGenreDto
                .builder()
                .id(bookGenreEntity.getId())
                .name(bookGenreEntity.getName())
                .build();
    }

    public static BookGenreEntity mapToEntity(BookGenreDto bookGenreDto){

        return BookGenreEntity
                .builder()
                .id(bookGenreDto.getId())
                .name(bookGenreDto.getName())
                .build();
    }

    public static List<BookGenreDto> mapToDtoList(List<BookGenreEntity> bookGenreEntities) {
        return bookGenreEntities.stream().map(BookGenreDto::mapToDto).collect(Collectors.toList());
    }

    public static List<BookGenreEntity> mapToEntityList(List<BookGenreDto> bookGenreDtos) {
        return bookGenreDtos.stream().map(BookGenreDto::mapToEntity).collect(Collectors.toList());
    }

}
