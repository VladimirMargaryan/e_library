package com.app.e_library.service.dto;

import com.app.e_library.persistence.entity.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;


@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private Long id;
    private String firstname;
    private String lastname;
    private String ssn;
    private String email;
    private String password;
    private Long registrationDate;
    private String phone;
    private AddressDto address;
    private UserStatusType status;
    private RoleDto role;

    public UserDto(Long id, String firstname, String lastname, String ssn,
                   String email, String phone, long registerDate,
                   String street, int streetNumber, String city, UserStatusType status) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.ssn = ssn;
        this.email = email;
        this.phone = phone;
        this.registrationDate = registerDate;

        this.address = AddressDto
                .builder()
                .city(CityDto.builder().cityName(city).build())
                .street(street)
                .streetNumber(streetNumber)
                .build();

        this.status = status;
    }

    public static UserDto mapToDto(UserEntity userEntity) {

       return UserDto
               .builder()
               .id(userEntity.getId())
               .firstname(userEntity.getFirstname())
               .lastname(userEntity.getLastname())
               .ssn(userEntity.getSsn())
               .email(userEntity.getEmail())
               .password(userEntity.getPassword())
               .registrationDate(userEntity.getRegistrationDate())
               .phone(userEntity.getPhone())
               .address(AddressDto.mapToDto(userEntity.getAddress()))
               .status(userEntity.getUserStatus())
               .role(RoleDto.mapToDto(userEntity.getRole()))
               .build();
    }

    public static UserEntity mapToEntity(UserDto userDto) {

        return UserEntity
                .builder()
                .id(userDto.getId())
                .firstname(userDto.getFirstname())
                .lastname(userDto.getLastname())
                .ssn(userDto.getSsn())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .registrationDate(userDto.getRegistrationDate())
                .phone(userDto.getPhone())
                .address(AddressDto.mapToEntity(userDto.getAddress()))
                .userStatus(userDto.getStatus())
                .role(RoleDto.mapToEntity(userDto.getRole()))
                .build();
    }

    public static List<UserDto> mapToDtoList(List<UserEntity> userEntities){
        return userEntities.stream().map(UserDto::mapToDto).collect(Collectors.toList());
    }

    public static Page<UserDto> mapToDtoPage(Page<UserEntity> userEntityPage) {
        return userEntityPage.map(UserDto::mapToDto);
    }
    public static List<UserEntity> mapToEntityList(List<UserDto> userDtos){
        return userDtos.stream().map(UserDto::mapToEntity).collect(Collectors.toList());
    }
}
