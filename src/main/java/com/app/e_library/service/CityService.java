package com.app.e_library.service;

import com.app.e_library.exception.NotFoundException;
import com.app.e_library.persistence.CityRepository;
import com.app.e_library.persistence.entity.CityEntity;
import com.app.e_library.service.dto.CityDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class CityService {

    private final CityRepository cityRepository;


    public List<CityDto> getAll() {
        return CityDto.mapToDtoList(cityRepository.findAll());
    }

    public CityDto getById(Long id) throws NotFoundException {
        return cityRepository.findById(id).map(CityDto::mapToDto)
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public CityDto save(CityDto cityDto) {
        CityEntity duplicateCity = cityRepository.getCityEntityByName(cityDto.getCityName());

        if (duplicateCity == null)
            return CityDto.mapToDto(cityRepository.save(CityDto.mapToEntityList(cityDto)));

        return CityDto.mapToDto(duplicateCity);
    }

    @Transactional
    public CityDto update(CityDto cityDto) {
        CityDto city = getById(cityDto.getId());
        city.setCityName(cityDto.getCityName());
        return CityDto.mapToDto(cityRepository.save(CityDto.mapToEntityList(cityDto)));
    }

    @Transactional
    public void deleteById(Long id) {
        cityRepository.deleteById(id);
    }
}
