package com.app.e_library.service;

import com.app.e_library.exception.NotFoundException;
import com.app.e_library.persistence.AddressRepository;
import com.app.e_library.persistence.CityRepository;
import com.app.e_library.persistence.RoleRepository;
import com.app.e_library.persistence.UserRepository;
import com.app.e_library.persistence.entity.AddressEntity;
import com.app.e_library.persistence.entity.CityEntity;
import com.app.e_library.persistence.entity.RoleEntity;
import com.app.e_library.persistence.entity.UserEntity;
import com.app.e_library.persistence.pagination.PageRequest;
import com.app.e_library.persistence.pagination.PageResponse;
import com.app.e_library.persistence.specification.UserSearchSpecification;
import com.app.e_library.service.dto.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final CityRepository cityRepository;
    private final AddressRepository addressRepository;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       RoleRepository roleRepository,
                       CityRepository cityRepository,
                       AddressRepository addressRepository) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.cityRepository = cityRepository;
        this.addressRepository = addressRepository;
    }


    public PageResponse<UserDto> getAllPaginated(int page,
                                                 int size,
                                                 String sortDirection,
                                                 String sortBy,
                                                 String filterBy,
                                                 String keyword) {

        Pageable pageRequest = PageRequest.buildPage(page, size, sortBy, sortDirection);
        Page<UserDto> userPage;

        if (keyword == null)
            userPage = UserDto.mapToDtoPage(userRepository.findAll(pageRequest));
        else {
            UserSearchSpecification searchSpecification = new UserSearchSpecification(keyword, filterBy);
            userPage = UserDto.mapToDtoPage(userRepository.findAll(searchSpecification, pageRequest));
        }

        return new PageResponse<>(userPage);
    }

    public UserDto getById(Long id) throws NotFoundException {
        return userRepository.findById(id).map(UserDto::mapToDto)
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public UserDto save(UserDto userDto) {
        UserDto duplicateUser = getByEmail(userDto.getEmail());

        if (duplicateUser == null){
            String encodedPassword = passwordEncoder.encode(userDto.getPassword());
            userDto.setPassword(encodedPassword);
            UserEntity saved = userRepository.save(UserDto.mapToEntity(userDto));
            return UserDto.mapToDto(saved);
        }
        return duplicateUser;
    }

    @Transactional
    public UserDto update(Long userId, UserDto userDto) {

        UserDto user = getById(userId);

        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setSsn(userDto.getSsn());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setRegistration_date(userDto.getRegistration_date());
        user.setPhone(userDto.getPhone());
        user.setResetPasswordToken(userDto.getResetPasswordToken());
        user.setResetPasswordTokenCreationDate(userDto.getResetPasswordTokenCreationDate());
        user.setAddress(userDto.getAddress());
        user.setStatus(userDto.getStatus());
        user.setRole(userDto.getRole());

        return UserDto.mapToDto(userRepository.save(UserDto.mapToEntity(user)));
    }

    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public UserDto getByEmail(String email) {
        UserEntity userByEmail = userRepository.getUserEntityByEmail(email);

        return userByEmail != null ? UserDto.mapToDto(userByEmail) : null;
    }

    private List<CSVRecord> readFile(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CSVParser csvParser =
                    CSVFormat.newFormat(';')
                            .withFirstRecordAsHeader()
                            .withIgnoreEmptyLines(true)
                            .withTrim()
                            .parse(reader);

            return csvParser.getRecords();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Transactional
    public void uploadUsers(MultipartFile usersFile) {

        List<CSVRecord> csvRecords = readFile(usersFile);

        List<UserEntity> userEntities = new ArrayList<>();
        List<AddressEntity> addressEntities = new ArrayList<>();
        List<CityEntity> cityEntities = new ArrayList<>();
        RoleEntity roleEntity = roleRepository.getRoleEntityByRollName(RoleType.ROLE_USER.name());

        UserEntity userEntity;
        if (csvRecords != null) {
            for (CSVRecord csvRecord : csvRecords) {

                String firstname = csvRecord.get("first_name");
                String lastname = csvRecord.get("last_name");
                String ssn = csvRecord.get("ssn");
                String email = csvRecord.get("email");
                String password = "password";
                long registrationDate = System.currentTimeMillis();
                String phoneNumber = csvRecord.get("phone");
                String city = csvRecord.get("city");
                String street = csvRecord.get("street");
                int streetNumber = Integer.parseInt(csvRecord.get("street_number"));


                CityEntity cityEntity = new CityEntity(city);
                AddressEntity addressEntity = new AddressEntity(cityEntity, street, streetNumber);

                cityEntities.add(cityEntity);
                addressEntities.add(addressEntity);

                userEntity = new UserEntity(
                        firstname,
                        lastname,
                        ssn,
                        email,
                        password,
                        registrationDate,
                        phoneNumber,
                        addressEntity,
                        UserStatusType.VERIFIED,
                        roleEntity
                );
                userEntities.add(userEntity);
            }
        }
        saveUsers(userEntities, addressEntities, cityEntities);
    }

    public void saveUsers(List<UserEntity> userEntities,
                                   List<AddressEntity> addressEntities,
                                   List<CityEntity> cityEntities) {

        // filtering and saving cities
        Map<String, CityEntity> savedCityEntities = cityRepository
                .saveAll(cityEntities.stream()
                        .filter(distinctByKey(CityEntity::getName))
                        .collect(Collectors.toList()))
                .stream()
                .collect(Collectors.toMap(CityEntity::getName, Function.identity()));

        // setting cities to addresses
        addressEntities.forEach(addressEntity -> addressEntity.setCity(savedCityEntities.get(addressEntity.getCity().getName())));

        // saving addresses
        List<AddressEntity> savedAddressEntities = addressRepository.saveAll(addressEntities);

        // saving users
        AtomicInteger i = new AtomicInteger();
        userEntities.forEach(userEntity -> userEntity.setAddress(savedAddressEntities.get(i.getAndIncrement())));
        userRepository.saveAll(userEntities);
    }

    private  <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> keySet = ConcurrentHashMap.newKeySet();
        return t -> keySet.add(keyExtractor.apply(t));
    }

}
