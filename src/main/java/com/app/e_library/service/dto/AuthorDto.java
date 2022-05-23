package com.app.e_library.service.dto;

import com.app.e_library.persistence.entity.AuthorEntity;
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
public class AuthorDto {

    private Long id;
    private String name;

    public AuthorDto(String name) {
        this.name = name;
    }

    public static AuthorDto mapToDto(AuthorEntity authorEntity){
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(authorEntity.getId());
        authorDto.setName(authorEntity.getName());

        return authorDto;
    }

    public static AuthorEntity mapToEntity(AuthorDto authorDto){
        AuthorEntity authorEntity = new AuthorEntity();
        authorEntity.setId(authorDto.getId());
        authorEntity.setName(authorDto.getName());

        return authorEntity;
    }

    public static List<AuthorDto> mapToDtoList(List<AuthorEntity> authorEntities){
        return authorEntities.stream().map(AuthorDto::mapToDto).collect(Collectors.toList());
    }

    public static List<AuthorEntity> mapToEntityList(List<AuthorDto> authorDtos){
        return authorDtos.stream().map(AuthorDto::mapToEntity).collect(Collectors.toList());
    }
}
