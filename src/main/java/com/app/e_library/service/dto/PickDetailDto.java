package com.app.e_library.service.dto;

import com.app.e_library.persistence.entity.BookEntity;
import com.app.e_library.persistence.entity.PickDetailEntity;
import com.app.e_library.persistence.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;


@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PickDetailDto {

    private Long id;
    private BookDto book;
    private UserDto usedByUser;
    private UserDto reservedByUser;
    private Long reservedUntil;
    private Long returnDate;

    public static PickDetailDto mapToDto(PickDetailEntity pickDetailEntity){
        return PickDetailDto
                .builder()
                .id(pickDetailEntity.getId())
                .book(BookDto.mapToDto(pickDetailEntity.getBook()))
                .usedByUser(UserDto.mapToDto(pickDetailEntity.getUsedByUser()))
                .reservedByUser(UserDto.mapToDto(pickDetailEntity.getReservedByUser()))
                .reservedUntil(pickDetailEntity.getReservedUntil())
                .returnDate(pickDetailEntity.getReturnDate())
                .build();
    }

    public static PickDetailEntity mapToEntity(PickDetailDto pickDetailDto){

        return PickDetailEntity
                .builder()
                .id(pickDetailDto.getId())
                .book(pickDetailDto.getBook() == null ? BookEntity
                        .builder()
                        .build() : BookDto.mapToEntity(pickDetailDto.getBook()))
                .usedByUser(pickDetailDto.getUsedByUser() == null ? UserEntity
                        .builder()
                        .build() : UserDto.mapToEntity(pickDetailDto.getUsedByUser()))
                .reservedByUser(pickDetailDto.getReservedByUser() == null ? UserEntity
                        .builder()
                        .build() : UserDto.mapToEntity(pickDetailDto.getReservedByUser()))
                .reservedUntil(pickDetailDto.getReservedUntil())
                .returnDate(pickDetailDto.getReturnDate())
                .build();
    }

    public static List<PickDetailDto> mapToDtoList(List<PickDetailEntity> borrowDetailEntities){
        return borrowDetailEntities.stream().map(PickDetailDto::mapToDto).collect(Collectors.toList());
    }

    public static List<PickDetailEntity> mapToEntityList(List<PickDetailDto> pickDetailDtos){
        return pickDetailDtos.stream().map(PickDetailDto::mapToEntity).collect(Collectors.toList());
    }
}
