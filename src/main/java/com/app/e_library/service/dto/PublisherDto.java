package com.app.e_library.service.dto;


import com.app.e_library.persistence.entity.PublisherEntity;
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
public class PublisherDto {

    private Long id;
    private String publisherName;

    public static PublisherDto mapToDto(PublisherEntity publisherEntity) {
        PublisherDto publisherDto = new PublisherDto();
        publisherDto.setId(publisherEntity.getId());
        publisherDto.setPublisherName(publisherEntity.getPublisherName());

        return publisherDto;
    }

    public static PublisherEntity mapToEntity(PublisherDto publisherDto) {

        PublisherEntity publisherEntity = new PublisherEntity();
        publisherEntity.setId(publisherDto.getId());
        publisherEntity.setPublisherName(publisherDto.getPublisherName());

        return publisherEntity;
    }

    public static List<PublisherDto> mapToDtoList(List<PublisherEntity> publisherEntities){
        return publisherEntities.stream().map(PublisherDto::mapToDto).collect(Collectors.toList());
    }

    public static List<PublisherEntity> mapToEntityList(List<PublisherDto> publisherDtos){
        return publisherDtos.stream().map(PublisherDto::mapToEntity).collect(Collectors.toList());
    }

}
