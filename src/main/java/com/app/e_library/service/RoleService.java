package com.app.e_library.service;


import com.app.e_library.exception.NotFoundException;
import com.app.e_library.persistence.RoleRepository;
import com.app.e_library.persistence.entity.RoleEntity;
import com.app.e_library.service.dto.RoleDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class RoleService{

    private final RoleRepository repository;


    public List<RoleDto> getAll() {
        return RoleDto.mapToDtoList(repository.findAll());
    }

    public RoleDto getById(Long id) throws NotFoundException {
        return repository.findById(id).map(RoleDto::mapToDto)
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public RoleDto save(RoleDto roleDto) {
        RoleEntity duplicateRole = repository.getRoleEntityByRollName(roleDto.getRollName());

        if (duplicateRole == null)
            return RoleDto.mapToDto(repository.save(RoleDto.mapToEntity(roleDto)));

        return RoleDto.mapToDto(duplicateRole);
    }

    @Transactional
    public RoleDto update(RoleDto roleDto) {
        RoleDto role = getById(roleDto.getId());
        role.setRollName(roleDto.getRollName());
        return RoleDto.mapToDto(repository.save(RoleDto.mapToEntity(roleDto)));
    }

    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
