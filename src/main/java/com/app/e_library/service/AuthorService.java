package com.app.e_library.service;

import com.app.e_library.exception.NotFoundException;
import com.app.e_library.persistence.AuthorRepository;
import com.app.e_library.persistence.entity.AuthorEntity;
import com.app.e_library.service.dto.AuthorDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public List<AuthorDto> getAll() {
        return AuthorDto.mapToDtoList(authorRepository.findAll());
    }

    public AuthorDto getById(Long id) throws NotFoundException {
        return authorRepository.findById(id).map(AuthorDto::mapToDto)
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public AuthorDto save(AuthorDto authorDto) {
        AuthorEntity duplicateAuthor = authorRepository.getAuthorEntityByName(authorDto.getName());

        if (duplicateAuthor == null){
            AuthorEntity saved = authorRepository.save(AuthorDto.mapToEntity(authorDto));
            return AuthorDto.mapToDto(saved);
        }
        return AuthorDto.mapToDto(duplicateAuthor);
    }

    @Transactional
    public AuthorDto update(AuthorDto authorDto) {
        AuthorDto author = getById(authorDto.getId());
        author.setName(authorDto.getName());
        return AuthorDto.mapToDto(authorRepository.save(AuthorDto.mapToEntity(author)));
    }

    @Transactional
    public void deleteById(Long id) {
        authorRepository.deleteById(id);
    }
}
