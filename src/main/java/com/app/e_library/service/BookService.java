package com.app.e_library.service;

import com.app.e_library.exception.NotFoundException;
import com.app.e_library.persistence.*;
import com.app.e_library.persistence.entity.*;
import com.app.e_library.persistence.pagination.PageRequest;
import com.app.e_library.persistence.pagination.PageResponse;
import com.app.e_library.service.dto.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookGenreRepository bookGenreRepository;
    private final PublisherRepository publisherRepository;
    private final AuthorRepository authorRepository;

    @Value("${book.image_folder.root}")
    private String coverImagesFolderPath;

    @Value("${book.thumbnail_folder.root}")
    private String thumbnailImagesFolderPath;

    public BookService(BookRepository bookRepository,
                       BookGenreRepository bookGenreRepository,
                       PublisherRepository publisherRepository,
                       AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.bookGenreRepository = bookGenreRepository;
        this.publisherRepository = publisherRepository;
        this.authorRepository = authorRepository;
    }


    public PageResponse<BookDto> getBooks(int page,
                                          int size,
                                          String sortDirection,
                                          String sortBy,
                                          String filterBy,
                                          String keyword) {

        Pageable pageRequest = PageRequest.buildPage(page, size, sortBy, sortDirection);
        Page<BookDto> bookPage = null;

        if (keyword == null)
            bookPage = BookDto.mapToDtoPage(bookRepository.findAll(pageRequest));
        else {
            if (filterBy != null) {
                switch (filterBy) {
                    case "genre":
                        bookPage = BookDto.mapToDtoPage(bookRepository.searchByGenre(pageRequest, keyword.toLowerCase().trim()));
                        break;
                    case "publisher":
                        bookPage = BookDto.mapToDtoPage(bookRepository.searchByPublisher(pageRequest, keyword.toLowerCase().trim()));
                        break;
                    case "author":
                        bookPage = BookDto.mapToDtoPage(bookRepository.searchByAuthor(pageRequest, keyword.toLowerCase().trim()));
                        break;
                }
            } else
                bookPage = BookDto.mapToDtoPage(bookRepository.searchByKeyword(pageRequest, keyword.toLowerCase().trim()));
        }

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

    private List<CSVRecord> readFile(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CSVParser csvParser =
                    CSVFormat.newFormat(';')
                            .withFirstRecordAsHeader()
                            .withIgnoreEmptyLines(true)
                            .withTrim()
                            .parse(reader);

            return csvParser.getRecords();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Transactional
    public void uploadBooks(MultipartFile booksFile) {

        List<CSVRecord> csvRecords = readFile(booksFile);

        Random random = new Random();

        List<BookEntity> bookEntities = new ArrayList<>();
        List<PublisherEntity> publisherEntities = new ArrayList<>();
        List<AuthorEntity> authorEntities = new ArrayList<>();
        List<BookGenreEntity> bookGenreEntities = bookGenreRepository.findAll();

        BookEntity bookEntity;
        if (csvRecords != null) {
            for (CSVRecord csvRecord : csvRecords){

                String isbn = csvRecord.get("ISBN").trim();
                String title = csvRecord.get("Book-Title").trim();
                String imageURLSmall = csvRecord.get("Image-URL-S").trim();
                String imageURLMedium = csvRecord.get("Image-URL-M").trim();
                String imageURLLarge = csvRecord.get("Image-URL-L").trim();
                String author = csvRecord.get("Book-Author").trim();
                short publicationYear = Short.parseShort(csvRecord.get("Year-Of-Publication"));
                String publisher = csvRecord.get("Publisher").trim();
                int pageCount = (int) ((Math.random() * (4000 - 50)) + 50);

                AuthorEntity authorEntity = new AuthorEntity(author);
                PublisherEntity publisherEntity = new PublisherEntity(publisher);
                BookGenreEntity bookGenreEntity = bookGenreEntities.get(random.nextInt(bookGenreEntities.size()));
                BookImageEntity bookImageEntity = new BookImageEntity(BookImageDownloadStatus.TODO, imageURLSmall, imageURLMedium, imageURLLarge);

                publisherEntities.add(publisherEntity);
                authorEntities.add(authorEntity);

                bookEntity = new BookEntity(
                        isbn,
                        title,
                        publicationYear,
                        pageCount,
                        bookGenreEntity,
                        BookStatusType.CHECKED_IN,
                        publisherEntity,
                        authorEntity,
                        bookImageEntity
                );

                bookEntities.add(bookEntity);

            }
        }
        saveBooks(bookEntities, publisherEntities, authorEntities);
    }

    public void saveBooks(List<BookEntity> bookEntities,
                          List<PublisherEntity> publisherEntities,
                          List<AuthorEntity>  authorEntities){

        // filtering Books
        List<BookEntity> uniqueBookEntities = bookEntities.stream()
                .filter(distinctByKey(bookDto ->
                        bookDto.getIsbn().toLowerCase()))
                .collect(Collectors.toList());

        // filtering and saving Publishers
        Map<String, PublisherEntity> savedPublisherEntities = publisherRepository
                .saveAll(publisherEntities.stream()
                        .filter(distinctByKey(publisherDto ->
                                publisherDto.getPublisherName().toLowerCase()))
                        .collect(Collectors.toList()))
                .stream()
                .collect(Collectors.toMap(publisherEntity ->
                        publisherEntity.getPublisherName().toLowerCase(), Function.identity()));

        // filtering and saving Authors
        Map<String, AuthorEntity> savedAuthorEntities = authorRepository
                .saveAll(authorEntities.stream()
                        .filter(distinctByKey(authorDto ->
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

    @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = Throwable.class)
    public void downloadBooksImages() throws IOException {


        List<BookEntity> firstNBooksByImageDownloadStatus =
                bookRepository.getFirstNBooksByImageDownloadStatus(Pageable.ofSize(10_000));

        firstNBooksByImageDownloadStatus.forEach(bookEntity -> {
            bookEntity.getBookImage().setImageDownloadStatus(BookImageDownloadStatus.IN_PROGRESS);
            bookEntity.getBookImage().setImageDownloadStartTime(System.currentTimeMillis());
        });

        if (!firstNBooksByImageDownloadStatus.isEmpty()){
            bookRepository.saveAll(firstNBooksByImageDownloadStatus);
            doDownload(firstNBooksByImageDownloadStatus);
            bookRepository.saveAll(firstNBooksByImageDownloadStatus);
        }
    }


    @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = Throwable.class)
    public void handleFailover() throws IOException {


        List<BookEntity> imageDownloadFailedBooks =
                bookRepository.getImageDownloadFailedBooks(System.currentTimeMillis(), Pageable.ofSize(100));

        imageDownloadFailedBooks.forEach(bookEntity ->
                bookEntity.getBookImage().setImageDownloadStartTime(System.currentTimeMillis()));

        if (!imageDownloadFailedBooks.isEmpty()) {
            bookRepository.saveAll(imageDownloadFailedBooks);
            doDownload(imageDownloadFailedBooks);
            bookRepository.saveAll(imageDownloadFailedBooks);
        }

    }

    public byte[] downloadImage(URL imageUrl) {

        byte[] imageByteArray = new byte[0];
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(imageUrl.openStream())){
            imageByteArray = IOUtils.toByteArray(bufferedInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageByteArray;
    }

    private Path createThumbnail(BufferedImage image,
                                 Path directory,
                                 String filename) throws IOException {

        int width = 128;
        int height = 128;
        Path thumbnailPath = Paths.get(directory + File.separator + "thumbnail-" + filename + ".jpg");

        double outputAspect = 1.0 * width / height;
        double inputAspect = 1.0 * image.getWidth() / image.getHeight();

        if (outputAspect < inputAspect)
            height = (int) (width / inputAspect);
        else
            width = (int) (height * inputAspect);

        BufferedImage thumbnail = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        thumbnail.createGraphics().drawImage(image.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
        ImageIO.write(thumbnail, "jpg", thumbnailPath.toFile());

        return thumbnailPath;
    }


    private void doDownload(List<BookEntity> bookEntitiesForImageDownload) throws IOException {

        Path coverImagesDirectory = Files.createDirectories(Paths.get(coverImagesFolderPath));
        Path thumbnailImagesDirectory = Files.createDirectories(Paths.get(thumbnailImagesFolderPath));

        bookEntitiesForImageDownload.forEach(bookEntity -> {

            String imageName = bookEntity.getId().toString();

            try {
                Path imagePath = Paths.get(coverImagesDirectory + File.separator + "image-" + imageName + ".jpg");
                byte[] imageByteArray = downloadImage(new URL(bookEntity.getBookImage().getImageURLLarge()));

                FileOutputStream outputStream = new FileOutputStream(imagePath.toString());
                outputStream.write(imageByteArray);

                File coverImage = imagePath.toFile();

                Path thumbnailPath = createThumbnail(
                        ImageIO.read(coverImage), thumbnailImagesDirectory, imageName);

                BookImageEntity bookImageEntity = new BookImageEntity();
                bookImageEntity.setCoverImagePath(imagePath.toString());
                bookImageEntity.setThumbnailPath(thumbnailPath.toString());
                bookImageEntity.setCoverImageSizeBytes(Files.size(imagePath));
                bookImageEntity.setThumbnailSizeBytes(Files.size(thumbnailPath));
                bookImageEntity.setType(FilenameUtils.getExtension(coverImage.getName()));

                bookEntity.setBookImage(bookImageEntity);
                bookEntity.getBookImage().setImageDownloadStatus(BookImageDownloadStatus.DONE);

            } catch (IOException e) {
                e.printStackTrace();
            }

        });

    }
    
    private <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> keySet = ConcurrentHashMap.newKeySet();
        return t -> keySet.add(keyExtractor.apply(t));
    }
}
