package com.app.e_library.service;

import com.app.e_library.exception.NotFoundException;
import com.app.e_library.persistence.AddressRepository;
import com.app.e_library.service.dto.AddressDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    public List<AddressDto> getAll() {
        return AddressDto.mapToDtoList(addressRepository.findAll());
    }

    public AddressDto getById(Long id) throws NotFoundException {
        return addressRepository.findById(id).map(AddressDto::mapToDto)
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public AddressDto save(AddressDto addressDto) {
            return AddressDto.mapToDto(addressRepository.save(AddressDto.mapToEntity(addressDto)));
    }

    @Transactional
    public AddressDto update(AddressDto addressDto) {
        AddressDto address = getById(addressDto.getId());
        address.setCity(addressDto.getCity());
        address.setStreet(addressDto.getStreet());
        address.setStreetNumber(addressDto.getStreetNumber());
        return AddressDto.mapToDto(addressRepository.save(AddressDto.mapToEntity(address)));
    }

    @Transactional
    public void deleteById(Long id) {
        addressRepository.deleteById(id);
    }
}
