package com.app.e_library.service;

import com.app.e_library.exception.NotFoundException;
import com.app.e_library.persistence.AuthorRepository;
import com.app.e_library.persistence.BookGenreRepository;
import com.app.e_library.persistence.BookRepository;
import com.app.e_library.persistence.PublisherRepository;
import com.app.e_library.persistence.entity.*;
import com.app.e_library.persistence.pagination.BookSearchCriteria;
import com.app.e_library.persistence.pagination.PageRequest;
import com.app.e_library.persistence.pagination.PageResponse;
import com.app.e_library.service.dto.BookDto;
import com.app.e_library.util.CsvFileReader;
import com.app.e_library.util.ImageUtil;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.app.e_library.service.dto.BookImageDownloadStatus.*;
import static com.app.e_library.service.dto.BookStatusType.CHECKED_IN;
import static com.app.e_library.util.ObjectUtil.distinctByField;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookGenreRepository bookGenreRepository;
    private final PublisherRepository publisherRepository;
    private final AuthorRepository authorRepository;
    private final CsvFileReader csvReader;
    private final ImageUtil imageUtil;

    @Value("${book.image_folder.root}")
    private String coverImagesFolderPath;

    @Value("${book.thumbnail_folder.root}")
    private String thumbnailImagesFolderPath;

    public BookService(BookRepository bookRepository,
                       BookGenreRepository bookGenreRepository,
                       PublisherRepository publisherRepository,
                       AuthorRepository authorRepository,
                       CsvFileReader csvReader,
                       ImageUtil imageUtil) {

        this.bookRepository = bookRepository;
        this.bookGenreRepository = bookGenreRepository;
        this.publisherRepository = publisherRepository;
        this.authorRepository = authorRepository;
        this.csvReader = csvReader;
        this.imageUtil = imageUtil;
    }


    public PageResponse<BookDto> getAllBooks(PageRequest pageRequest) {
        return new PageResponse<>(BookDto.mapToDtoPage(bookRepository.findAll(pageRequest.getPageable())));
    }


    public PageResponse<BookDto> searchBooks(BookSearchCriteria bookSearchCriteria) {

        Page<BookDto> bookPage = bookRepository.searchBook(
                bookSearchCriteria.getIsbn(),
                bookSearchCriteria.getTitle(),
                bookSearchCriteria.getPublicationYear(),
                bookSearchCriteria.getGenre(),
                bookSearchCriteria.getAuthor(),
                bookSearchCriteria.getPublisher(),
                bookSearchCriteria.getPageable()
        );

        return new PageResponse<>(bookPage);
    }

    public BookDto getById(Long id) throws NotFoundException {
        return bookRepository.findById(id).map(BookDto::mapToDto)
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public BookDto save(BookDto bookDto) {

        Optional<BookEntity> duplicateBook = bookRepository.findBookEntityByIsbn(bookDto.getIsbn());

        if (!duplicateBook.isPresent()) {
            BookEntity saved = bookRepository.save(BookDto.mapToEntity(bookDto));
            return BookDto.mapToDto(saved);
        }
        return BookDto.mapToDto(duplicateBook.get());
    }

    @Transactional
    public BookDto update(Long bookId, BookDto bookDto) {
        BookDto book = getById(bookId);
        book.setIsbn(bookDto.getIsbn());
        book.setTitle(bookDto.getTitle());
        book.setPublicationYear(bookDto.getPublicationYear());
        book.setPageCount(bookDto.getPageCount());
        book.setBookGenre(bookDto.getBookGenre());
        book.setBookStatus(bookDto.getBookStatus());
        book.setPublisher(bookDto.getPublisher());
        book.setPickDetail(bookDto.getPickDetail());
        book.setAuthor(bookDto.getAuthor());
        book.setImageDto(bookDto.getImageDto());
        return BookDto.mapToDto(bookRepository.save(BookDto.mapToEntity(book)));
    }

    @Transactional
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    public void uploadBooks(MultipartFile booksFile) {

        List<CSVRecord> csvRecords = csvReader.readFile(booksFile);

        Random random = new Random();

        List<BookEntity> bookEntities = new ArrayList<>();
        List<PublisherEntity> publisherEntities = new ArrayList<>();
        List<AuthorEntity> authorEntities = new ArrayList<>();
        List<BookGenreEntity> bookGenreEntities = bookGenreRepository.findAll();

        BookEntity bookEntity;
        if (csvRecords != null) {
            for (CSVRecord csvRecord : csvRecords) {

                String isbn = csvRecord.get("ISBN").trim();
                String title = csvRecord.get("Book-Title").trim();
                String imageURLSmall = csvRecord.get("Image-URL-S").trim();
                String imageURLMedium = csvRecord.get("Image-URL-M").trim();
                String imageURLLarge = csvRecord.get("Image-URL-L").trim();
                String author = csvRecord.get("Book-Author").trim();
                short publicationYear = Short.parseShort(csvRecord.get("Year-Of-Publication"));
                String publisher = csvRecord.get("Publisher").trim();
                int pageCount = (int) ((Math.random() * (4000 - 50)) + 50);

                AuthorEntity authorEntity = AuthorEntity.builder()
                        .name(author)
                        .build();

                PublisherEntity publisherEntity = PublisherEntity.builder()
                        .publisherName(publisher)
                        .build();

                BookGenreEntity bookGenreEntity = bookGenreEntities.get(random.nextInt(bookGenreEntities.size()));

                BookImageEntity bookImageEntity = BookImageEntity.builder()
                        .imageDownloadStatus(TODO)
                        .imageURLSmall(imageURLSmall)
                        .imageURLMedium(imageURLMedium)
                        .imageURLLarge(imageURLLarge)
                        .build();

                bookEntity = BookEntity.builder()
                        .isbn(isbn)
                        .title(title)
                        .publicationYear(publicationYear)
                        .pageCount(pageCount)
                        .bookGenre(bookGenreEntity)
                        .bookStatus(CHECKED_IN)
                        .publisher(publisherEntity)
                        .author(authorEntity)
                        .bookImage(bookImageEntity)
                        .build();

                bookEntities.add(bookEntity);
                publisherEntities.add(publisherEntity);
                authorEntities.add(authorEntity);

            }
        }
        saveBooks(bookEntities, publisherEntities, authorEntities);
    }

    public void saveBooks(List<BookEntity> bookEntities,
                          List<PublisherEntity> publisherEntities,
                          List<AuthorEntity> authorEntities) {

        // filtering Books
        List<BookEntity> uniqueBookEntities = bookEntities.stream()
                .filter(distinctByField(bookDto ->
                        bookDto.getIsbn().toLowerCase()))
                .collect(Collectors.toList());

        // filtering and saving Publishers
        Map<String, PublisherEntity> savedPublisherEntities = publisherRepository
                .saveAll(publisherEntities.stream()
                        .filter(distinctByField(publisherDto ->
                                publisherDto.getPublisherName().toLowerCase()))
                        .collect(Collectors.toList()))
                .stream()
                .collect(Collectors.toMap(publisherEntity ->
                        publisherEntity.getPublisherName().toLowerCase(), Function.identity()));

        // filtering and saving Authors
        Map<String, AuthorEntity> savedAuthorEntities = authorRepository
                .saveAll(authorEntities.stream()
                        .filter(distinctByField(authorDto ->
                                authorDto.getName().toLowerCase()))
                        .collect(Collectors.toList()))
                .stream()
                .collect(Collectors.toMap(authorEntity ->
                        authorEntity.getName().toLowerCase(), Function.identity()));

        // setting saved publishers and authors to books
        uniqueBookEntities.forEach(bookEntity -> {
            bookEntity.setPublisher(savedPublisherEntities.get(bookEntity.getPublisher().getPublisherName().toLowerCase()));
            bookEntity.setAuthor(savedAuthorEntities.get(bookEntity.getAuthor().getName().toLowerCase()));
        });

        // saving books
        bookRepository.saveAll(uniqueBookEntities);
    }

    public byte[] getImageByteArrayFromUrl(URL imageUrl) {
        return imageUtil.getImageByteArray(imageUrl);
    }

    public void downloadBooksImages() throws IOException {


        List<BookEntity> firstNBooksByImageDownloadStatus = bookRepository
                .getFirstNBooksByImageDownloadStatus(System.currentTimeMillis(), Pageable.ofSize(500));

        firstNBooksByImageDownloadStatus.forEach(bookEntity -> {
            bookEntity.getBookImage().setImageDownloadStatus(IN_PROGRESS);
            bookEntity.getBookImage().setImageDownloadStartTime(System.currentTimeMillis());
        });

        if (!firstNBooksByImageDownloadStatus.isEmpty()) {
            bookRepository.saveAll(firstNBooksByImageDownloadStatus);
            downloadBookImage(firstNBooksByImageDownloadStatus);
        }
    }


    @Transactional(rollbackFor = Throwable.class)
    public void downloadBookImage(List<BookEntity> bookEntitiesForImageDownload) throws IOException {

        Path coverImagesDirectory = Files.createDirectories(Paths.get(coverImagesFolderPath));
        Path thumbnailImagesDirectory = Files.createDirectories(Paths.get(thumbnailImagesFolderPath));

        bookEntitiesForImageDownload.forEach(bookEntity -> {

            BookImageEntity bookImageEntity = bookEntity.getBookImage();
            long bookId = bookEntity.getId();

            try {
                URL imageUrl = new URL(bookEntity.getBookImage().getImageURLLarge());
                byte[] imageByteArray = imageUtil.getImageByteArray(imageUrl, "jpg");

                Path coverImagePath = imageUtil
                        .saveImageLocally(coverImagesDirectory,
                                "image-" + bookId,
                                imageUrl,
                                imageByteArray);

                if (coverImagePath != null) {
                    File coverImage = coverImagePath.toFile();
                    String imageExtension = FilenameUtils.getExtension(coverImage.getName());

                    Path thumbnailPath = imageUtil
                            .createThumbnailImage(ImageIO.read(coverImage),
                                    thumbnailImagesDirectory,
                                    "thumbnail-" + bookId,
                                    imageExtension);

                    bookImageEntity.setType(imageExtension);
                    bookImageEntity.setCoverImagePath(coverImagePath.toString());
                    bookImageEntity.setThumbnailPath(thumbnailPath.toString());
                    bookImageEntity.setCoverImageSizeBytes(coverImage.length());
                    bookImageEntity.setThumbnailSizeBytes(thumbnailPath.toFile().length());
                    bookImageEntity.setImageDownloadStatus(DONE);

                } else {
                    bookEntity.getBookImage().setImageDownloadStartTime(null);
                    bookImageEntity.setImageDownloadStatus(NOT_SUPPORTED);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        bookRepository.saveAll(bookEntitiesForImageDownload);

    }
}
