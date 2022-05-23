package com.app.e_library.service.dto;


import com.app.e_library.persistence.entity.ReceiptEntity;
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
public class ReceiptDto {

    private Long id;
    private Long orderDate;
    private Long expirationDate;
    private UserDto user;
    private BookDto book;


    public static ReceiptDto mapToDto(ReceiptEntity receiptEntity) {
        return new ReceiptDto(
                receiptEntity.getId(),
                receiptEntity.getOrderDate(),
                receiptEntity.getExpirationDate(),
                UserDto.mapToDto(receiptEntity.getUser()),
                BookDto.mapToDto(receiptEntity.getBook())
        );
    }

    public static ReceiptEntity mapToEntity(ReceiptDto receiptDto) {
        return new ReceiptEntity(
                receiptDto.getId(),
                receiptDto.getOrderDate(),
                receiptDto.getExpirationDate(),
                UserDto.mapToEntity(receiptDto.getUser()),
                BookDto.mapToEntity(receiptDto.getBook())
        );
    }

    public static List<ReceiptDto> mapToDtoList(List<ReceiptEntity> receiptEntities){
        return receiptEntities.stream().map(ReceiptDto::mapToDto).collect(Collectors.toList());
    }

    public static List<ReceiptEntity> mapToEntityList(List<ReceiptDto> receiptDtos){
        return receiptDtos.stream().map(ReceiptDto::mapToEntity).collect(Collectors.toList());
    }
}
