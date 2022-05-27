package com.app.e_library.service.dto;

import com.app.e_library.persistence.entity.AddressEntity;
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
public class AddressDto {

    private Long id;
    private CityDto city;
    private String street;
    private int streetNumber;

    public AddressDto(CityDto city, String street, int streetNumber) {
        this.city = city;
        this.street = street;
        this.streetNumber = streetNumber;
    }

    public static AddressDto mapToDto(AddressEntity addressEntity){
        AddressDto addressDto = new AddressDto();
        addressDto.setId(addressEntity.getId());
        addressDto.setCity(CityDto.mapToDto(addressEntity.getCity()));
        addressDto.setStreet(addressEntity.getStreet());
        addressDto.setStreetNumber(addressEntity.getStreetNumber());

        return addressDto;
    }

    public static AddressEntity mapToEntity(AddressDto addressDto){
        AddressEntity address = new AddressEntity();
        address.setId(addressDto.getId());
        address.setCity(CityDto.mapToEntityList(addressDto.getCity()));
        address.setStreet(addressDto.getStreet());
        address.setStreetNumber(addressDto.getStreetNumber());
        return address;

    }

    public static List<AddressDto> mapToDtoList(List<AddressEntity> addressEntities){
        return addressEntities.stream().map(AddressDto::mapToDto).collect(Collectors.toList());
    }

    public static List<AddressEntity> mapToEntity(List<AddressDto> addressDtos){
        return addressDtos.stream().map(AddressDto::mapToEntity).collect(Collectors.toList());
    }
}
