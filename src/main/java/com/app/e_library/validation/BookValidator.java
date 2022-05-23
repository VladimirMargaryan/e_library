package com.app.e_library.validation;

import com.app.e_library.service.dto.AuthorDto;
import com.app.e_library.service.dto.BookDto;
import com.app.e_library.service.dto.BookGenreDto;
import com.app.e_library.service.dto.PublisherDto;

import java.time.LocalDate;

public class BookValidator {

    public static boolean isValid(BookDto book){
        return (book.getIsbn() != null && !book.getIsbn().isEmpty())
                && (book.getTitle() != null && !book.getTitle().isEmpty())
                && (book.getPublicationYear() > 0 && book.getPublicationYear() <= LocalDate.now().getYear())
                && (book.getPageCount() >= 50 && book.getPageCount() <= 5000)
                && (book.getAuthor() != null && isValidAuthor(book.getAuthor()))
                && (book.getPublisher() != null && isValidPublisher(book.getPublisher()))
                && (book.getBookGenre() != null && isValidBookGenre(book.getBookGenre()));
    }

    private static boolean isValidAuthor(AuthorDto authorDto) {
        return authorDto.getName() != null && ! authorDto.getName().isEmpty();
    }

    private static boolean isValidPublisher(PublisherDto publisherDto) {
        return publisherDto.getPublisherName() != null && !publisherDto.getPublisherName().isEmpty();
    }

    private static boolean isValidBookGenre(BookGenreDto bookGenreDto) {
        return bookGenreDto.getName() != null && !bookGenreDto.getName().isEmpty();
    }


}
