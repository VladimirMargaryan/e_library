package com.app.e_library.service.dto;

import com.app.e_library.persistence.entity.CityEntity;
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
public class CityDto {

    private Long id;
    private String cityName;

    public static CityDto mapToDto(CityEntity cityEntity){
        return new CityDto(
                cityEntity.getId(),
                cityEntity.getName()
        );
    }

    public static CityEntity mapToEntityList(CityDto cityDto){
        return new CityEntity(
                cityDto.getId(),
                cityDto.getCityName()
        );
    }

    public static List<CityDto> mapToDtoList(List<CityEntity> cityEntities){
        return cityEntities.stream().map(CityDto::mapToDto).collect(Collectors.toList());
    }

    public static List<CityEntity> toCityEntityList(List<CityDto> cityDtos){
        return cityDtos.stream().map(CityDto::mapToEntityList).collect(Collectors.toList());
    }

}
