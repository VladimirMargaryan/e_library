package com.app.e_library.service;

import com.app.e_library.exception.NotFoundException;
import com.app.e_library.persistence.PickDetailRepository;
import com.app.e_library.service.dto.PickDetailDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class PickDetailService {

    private final PickDetailRepository pickDetailRepository;

    public PickDetailService(PickDetailRepository pickDetailRepository) {
        this.pickDetailRepository = pickDetailRepository;
    }

    public List<PickDetailDto> getAll() {
        return PickDetailDto.mapToDtoList(pickDetailRepository.findAll());
    }

    public PickDetailDto getById(Long id) throws NotFoundException {
        return pickDetailRepository.findById(id).map(PickDetailDto::mapToDto)
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public PickDetailDto save(PickDetailDto pickDetailDto) {
        return PickDetailDto
                .mapToDto(pickDetailRepository
                        .save(PickDetailDto.mapToEntity(pickDetailDto)));
    }

    @Transactional
    public PickDetailDto update(PickDetailDto pickDetailDto) {

        PickDetailDto pickDetail = getById(pickDetailDto.getId());
        pickDetail.setBook(pickDetailDto.getBook());
        pickDetail.setReturnDate(pickDetailDto.getReturnDate());
        pickDetail.setReservedUntil(pickDetailDto.getReservedUntil());
        pickDetail.setReservedByUser(pickDetailDto.getReservedByUser());
        pickDetail.setUsedByUser(pickDetailDto.getUsedByUser());

        return PickDetailDto
                .mapToDto(pickDetailRepository
                        .save(PickDetailDto.mapToEntity(pickDetail)));
    }

    @Transactional
    public void deleteById(Long id) {
        pickDetailRepository.deleteById(id);
    }
}
