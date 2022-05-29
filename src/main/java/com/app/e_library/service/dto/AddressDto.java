package com.app.e_library.service.dto;

import com.app.e_library.persistence.entity.AddressEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressDto {

    private Long id;
    private CityDto city;
    private String street;
    private int streetNumber;


    public static AddressDto mapToDto(AddressEntity addressEntity){

        return AddressDto.builder()
                .id(addressEntity.getId())
                .city(CityDto.mapToDto(addressEntity.getCity()))
                .street(addressEntity.getStreet())
                .streetNumber(addressEntity.getStreetNumber())
                .build();
    }

    public static AddressEntity mapToEntity(AddressDto addressDto){

        return AddressEntity
                .builder()
                .id(addressDto.getId())
                .city(CityDto.mapToEntityList(addressDto.getCity()))
                .street(addressDto.getStreet())
                .streetNumber(addressDto.getStreetNumber())
                .build();

    }

    public static List<AddressDto> mapToDtoList(List<AddressEntity> addressEntities){
        return addressEntities.stream().map(AddressDto::mapToDto).collect(Collectors.toList());
    }

    public static List<AddressEntity> mapToEntity(List<AddressDto> addressDtos){
        return addressDtos.stream().map(AddressDto::mapToEntity).collect(Collectors.toList());
    }
}
