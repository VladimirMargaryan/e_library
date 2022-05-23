package com.app.e_library.service;

import com.app.e_library.exception.NotFoundException;
import com.app.e_library.persistence.BookGenreRepository;
import com.app.e_library.persistence.entity.BookGenreEntity;
import com.app.e_library.service.dto.BookGenreDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class BookGenreService {

    private final BookGenreRepository bookGenreRepository;

    public BookGenreService(BookGenreRepository bookGenreRepository) {
        this.bookGenreRepository = bookGenreRepository;
    }

    public List<BookGenreDto> getAll() {
        return BookGenreDto.mapToDtoList(bookGenreRepository.findAll());
    }

    public BookGenreDto getById(Long id) throws NotFoundException {
        return bookGenreRepository.findById(id).map(BookGenreDto::mapToDto)
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public BookGenreDto save(BookGenreDto bookGenreDto) {

        BookGenreEntity duplicateBookGenre =
                bookGenreRepository.getBookGenreEntityByName(bookGenreDto.getName());

        if (duplicateBookGenre == null)
            return BookGenreDto.mapToDto(bookGenreRepository.save(BookGenreDto.mapToEntity(bookGenreDto)));

        return BookGenreDto.mapToDto(duplicateBookGenre);
    }

    @Transactional
    public BookGenreDto update(BookGenreDto bookGenreDto) {
        BookGenreDto BookGenre = getById(bookGenreDto.getId());
        BookGenre.setName(bookGenreDto.getName());
        return BookGenreDto.mapToDto(bookGenreRepository.save(BookGenreDto.mapToEntity(BookGenre)));
    }

    @Transactional
    public void removeById(Long id) {
        bookGenreRepository.deleteById(id);
    }
}
