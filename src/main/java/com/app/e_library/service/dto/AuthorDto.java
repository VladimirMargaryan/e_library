package com.app.e_library.service.dto;

import com.app.e_library.persistence.entity.AuthorEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@JsonInclude(NON_NULL)
public class AuthorDto {

    private Long id;
    private String name;

    public static AuthorDto mapToDto(AuthorEntity authorEntity){

        return AuthorDto
                .builder()
                .id(authorEntity.getId())
                .name(authorEntity.getName())
                .build();
    }

    public static AuthorEntity mapToEntity(AuthorDto authorDto){

        return AuthorEntity
                .builder()
                .id(authorDto.getId())
                .name(authorDto.getName())
                .build();
    }

    public static List<AuthorDto> mapToDtoList(List<AuthorEntity> authorEntities){
        return authorEntities.stream().map(AuthorDto::mapToDto).collect(Collectors.toList());
    }

    public static List<AuthorEntity> mapToEntityList(List<AuthorDto> authorDtos){
        return authorDtos.stream().map(AuthorDto::mapToEntity).collect(Collectors.toList());
    }
}
