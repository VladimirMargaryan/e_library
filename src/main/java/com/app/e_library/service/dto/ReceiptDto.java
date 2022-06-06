package com.app.e_library.service.dto;


import com.app.e_library.persistence.entity.ReceiptEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;


@Data
@Builder
@JsonInclude(NON_NULL)
public class ReceiptDto {

    private Long id;
    private Long orderDate;
    private Long expirationDate;
    private UserDto user;
    private BookDto book;


    public static ReceiptDto mapToDto(ReceiptEntity receiptEntity) {

        return ReceiptDto
                .builder()
                .id(receiptEntity.getId())
                .orderDate(receiptEntity.getOrderDate())
                .expirationDate(receiptEntity.getExpirationDate())
                .user(UserDto.mapToDto(receiptEntity.getUser()))
                .book(BookDto.mapToDto(receiptEntity.getBook()))
                .build();
    }

    public static ReceiptEntity mapToEntity(ReceiptDto receiptDto) {

        return ReceiptEntity
                .builder()
                .id(receiptDto.getId())
                .orderDate(receiptDto.getOrderDate())
                .expirationDate(receiptDto.getExpirationDate())
                .user(UserDto.mapToEntity(receiptDto.getUser()))
                .book(BookDto.mapToEntity(receiptDto.getBook()))
                .build();

    }

    public static List<ReceiptDto> mapToDtoList(List<ReceiptEntity> receiptEntities){
        return receiptEntities.stream().map(ReceiptDto::mapToDto).collect(Collectors.toList());
    }

    public static List<ReceiptEntity> mapToEntityList(List<ReceiptDto> receiptDtos){
        return receiptDtos.stream().map(ReceiptDto::mapToEntity).collect(Collectors.toList());
    }
}
