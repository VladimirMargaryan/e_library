package com.app.e_library.service;

import com.app.e_library.exception.NotFoundException;
import com.app.e_library.persistence.PublisherRepository;
import com.app.e_library.persistence.entity.PublisherEntity;
import com.app.e_library.service.dto.PublisherDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class PublisherService {

    private final PublisherRepository publisherRepository;

    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public List<PublisherDto> getAll() {
        return PublisherDto.mapToDtoList(publisherRepository.findAll());
    }

    public PublisherDto getById(Long id) throws NotFoundException {
        return publisherRepository.findById(id).map(PublisherDto::mapToDto)
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public PublisherDto save(PublisherDto publisherDto) {
        PublisherEntity duplicatePublisher =
                publisherRepository.getPublisherEntityByPublisherName(publisherDto.getPublisherName());

        if (duplicatePublisher == null)
            return PublisherDto.mapToDto(publisherRepository.save(PublisherDto.mapToEntity(publisherDto)));

        return PublisherDto.mapToDto(duplicatePublisher);
    }

    @Transactional
    public PublisherDto update(PublisherDto publisherDto) {
        PublisherDto publisher = getById(publisherDto.getId());
        publisher.setPublisherName(publisherDto.getPublisherName());
        return PublisherDto.mapToDto(publisherRepository.save(PublisherDto.mapToEntity(publisherDto)));
    }

    @Transactional
    public void deleteById(Long id) {
        publisherRepository.deleteById(id);
    }
}
