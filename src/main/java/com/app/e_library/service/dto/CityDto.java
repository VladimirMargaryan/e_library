package com.app.e_library.service.dto;

import com.app.e_library.persistence.entity.CityEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@JsonInclude(NON_NULL)
public class CityDto {

    private Long id;
    private String cityName;


    public static CityDto mapToDto(CityEntity cityEntity){

        return CityDto
                .builder()
                .id(cityEntity.getId())
                .cityName(cityEntity.getName())
                .build();
    }

    public static CityEntity mapToEntityList(CityDto cityDto){

        return CityEntity
                .builder()
                .id(cityDto.getId())
                .name(cityDto.getCityName())
                .build();
    }

    public static List<CityDto> mapToDtoList(List<CityEntity> cityEntities){
        return cityEntities.stream().map(CityDto::mapToDto).collect(Collectors.toList());
    }

    public static List<CityEntity> toCityEntityList(List<CityDto> cityDtos){
        return cityDtos.stream().map(CityDto::mapToEntityList).collect(Collectors.toList());
    }

}
