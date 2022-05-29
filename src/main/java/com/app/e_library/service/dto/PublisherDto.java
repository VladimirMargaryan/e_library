package com.app.e_library.service.dto;


import com.app.e_library.persistence.entity.PublisherEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PublisherDto {

    private Long id;
    private String publisherName;


    public static PublisherDto mapToDto(PublisherEntity publisherEntity) {

        return PublisherDto
                .builder()
                .id(publisherEntity.getId())
                .publisherName(publisherEntity.getPublisherName())
                .build();
    }

    public static PublisherEntity mapToEntity(PublisherDto publisherDto) {

        return PublisherEntity
                .builder()
                .id(publisherDto.getId())
                .publisherName(publisherDto.getPublisherName())
                .build();
    }

    public static List<PublisherDto> mapToDtoList(List<PublisherEntity> publisherEntities){
        return publisherEntities.stream().map(PublisherDto::mapToDto).collect(Collectors.toList());
    }

    public static List<PublisherEntity> mapToEntityList(List<PublisherDto> publisherDtos){
        return publisherDtos.stream().map(PublisherDto::mapToEntity).collect(Collectors.toList());
    }

}
