package com.app.e_library.service.dto;


import com.app.e_library.persistence.entity.RoleEntity;
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
public class RoleDto {

    private Long id;
    private String rollName;

    public static RoleDto mapToDto(RoleEntity roleEntity){

        return new RoleDto(
                roleEntity.getId(),
                roleEntity.getRollName()
        );
    }

    public static RoleEntity mapToEntity(RoleDto roleDto){

        return new RoleEntity(
                roleDto.getId(),
                roleDto.getRollName()
        );
    }

    public static List<RoleDto> mapToDtoList(List<RoleEntity> roleEntities) {
        return roleEntities.stream().map(RoleDto::mapToDto).collect(Collectors.toList());
    }

    public static List<RoleEntity> mapToEntityList(List<RoleDto> roleDtos) {
        return roleDtos.stream().map(RoleDto::mapToEntity).collect(Collectors.toList());
    }

}
