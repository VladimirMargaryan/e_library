package com.app.e_library.controller;

import com.app.e_library.persistence.pagination.PageResponse;
import com.app.e_library.service.BookService;
import com.app.e_library.service.dto.BookDto;
import com.app.e_library.validation.BookValidator;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/parse/csv")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> uploadBooks(@RequestParam("books") MultipartFile books) {
        if (books.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Bad request!");
        } else if(!Objects.equals(books.getContentType(), "text/csv")){
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();
        }
        bookService.uploadBooks(books);
        return ResponseEntity.ok().body("Books saved successfully!");
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'USER')")
    public ResponseEntity<Page<BookDto>> getBooksPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(required = false) String filterBy,
            @RequestParam(name = "sortDir", defaultValue = "asc") String sortDirection,
            @RequestParam(required = false) String keyword) {

        PageResponse<BookDto> bookPageResponse = bookService.getBooks(page, size, sortDirection, sortBy, filterBy, keyword);
        HttpHeaders responseHeaders = new HttpHeaders(bookPageResponse.buildHttpHeadersForPages());

        return ResponseEntity.ok().headers(responseHeaders).body(bookPageResponse.getPage());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('EMPLOYEE')")
    public ResponseEntity<BookDto> createBook(@RequestBody BookDto book) {
        if (BookValidator.isValid(book)) {
            BookDto savedBookDto = bookService.save(book);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBookDto);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{bookId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'USER')")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long bookId){
        BookDto bookById = bookService.getById(bookId);
        return ResponseEntity.ok().body(bookById);
    }

    @PutMapping("/{bookId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE')")
    public ResponseEntity<BookDto> updateBook(@PathVariable Long bookId, @RequestBody BookDto book){
        if (BookValidator.isValid(book)){
            BookDto updatedBook = bookService.update(bookId, book);
            return ResponseEntity.ok().body(updatedBook);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{bookId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE')")
    public ResponseEntity<?> deleteBook(@PathVariable Long bookId){
        bookService.deleteById(bookId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/image",
            produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE})
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'USER')")
    public ResponseEntity<byte[]> downloadBookImage(String url) throws MalformedURLException {
        return ResponseEntity.ok().body(bookService.downloadImage(new URL(url)));
    }

}
