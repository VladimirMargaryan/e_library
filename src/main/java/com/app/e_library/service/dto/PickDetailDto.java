package com.app.e_library.service.dto;

import com.app.e_library.persistence.entity.PickDetailEntity;
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
public class PickDetailDto {

    private Long id;
    private BookDto book;
    private UserDto usedByUser;
    private UserDto reservedByUser;
    private Long reservedUntil;
    private Long returnDate;

    public static PickDetailDto mapToDto(PickDetailEntity pickDetailEntity){
        return new PickDetailDto(
                pickDetailEntity.getId(),
                BookDto.mapToDto(pickDetailEntity.getBook()),
                UserDto.mapToDto(pickDetailEntity.getUsedByUser()),
                UserDto.mapToDto(pickDetailEntity.getReservedByUser()),
                pickDetailEntity.getReservedUntil(),
                pickDetailEntity.getReturnDate()
        );
    }

    public static PickDetailEntity mapToEntity(PickDetailDto pickDetailDto){

        PickDetailEntity newPickDetailEntity =  new PickDetailEntity(
                pickDetailDto.getId(),
                pickDetailDto.getReservedUntil(),
                pickDetailDto.getReturnDate()
        );

        if (pickDetailDto.getReservedByUser() != null)
            newPickDetailEntity.setReservedByUser(UserDto.mapToEntity(pickDetailDto.getReservedByUser()));

        if (pickDetailDto.getBook() != null)
            newPickDetailEntity.setBook(BookDto.mapToEntity(pickDetailDto.getBook()));

        if (pickDetailDto.getUsedByUser() != null)
            newPickDetailEntity.setUsedByUser(UserDto.mapToEntity(pickDetailDto.getUsedByUser()));

        return newPickDetailEntity;
    }

    public static List<PickDetailDto> mapToDtoList(List<PickDetailEntity> borrowDetailEntities){
        return borrowDetailEntities.stream().map(PickDetailDto::mapToDto).collect(Collectors.toList());
    }

    public static List<PickDetailEntity> mapToEntityList(List<PickDetailDto> pickDetailDtos){
        return pickDetailDtos.stream().map(PickDetailDto::mapToEntity).collect(Collectors.toList());
    }
}
