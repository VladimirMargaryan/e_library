package com.app.e_library.service.dto;

import com.app.e_library.persistence.entity.*;
import com.app.e_library.validation.ValidEmail;
import com.app.e_library.validation.ValidPassword;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private Long id;
    private String firstname;
    private String lastname;
    private String ssn;
    private String email;
    private String password;
    private Long registration_date;
    private String phone;
    @JsonIgnore
    private String resetPasswordToken;
    @JsonIgnore
    private Long resetPasswordTokenCreationDate;
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
        this.registration_date = registerDate;
        this.address = new AddressDto(new CityDto(city), street, streetNumber);
        this.status = status;
    }

    public static UserDto mapToDto(UserEntity userEntity) {

        return new UserDto(
                userEntity.getId(),
                userEntity.getFirstname(),
                userEntity.getLastname(),
                userEntity.getSsn(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getRegistration_date(),
                userEntity.getPhone(),
                userEntity.getResetPasswordToken(),
                userEntity.getResetPasswordTokenCreationDate(),
                AddressDto.mapToDto(userEntity.getAddress()),
                userEntity.getUserStatus(),
                RoleDto.mapToDto(userEntity.getRole())
        );
    }

    public static UserEntity mapToEntity(UserDto userDto) {

        return new UserEntity(
                userDto.getId(),
                userDto.getFirstname(),
                userDto.getLastname(),
                userDto.getSsn(),
                userDto.getEmail(),
                userDto.getPassword(),
                userDto.getRegistration_date(),
                userDto.getPhone(),
                userDto.getResetPasswordToken(),
                userDto.getResetPasswordTokenCreationDate(),
                AddressDto.mapToEntity(userDto.getAddress()),
                userDto.getStatus(),
                RoleDto.mapToEntity(userDto.getRole())
        );
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
