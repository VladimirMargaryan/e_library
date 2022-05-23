package com.app.e_library.schedule;

import com.app.e_library.service.BookService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;

@Configuration
public class BookImageDownloadSchedule {

    private final BookService bookService;

    public BookImageDownloadSchedule(BookService bookService) {
        this.bookService = bookService;
    }

    @Scheduled(fixedRateString = "${job.imageDownloader.fixedRate}")
    public void downloadBookImages() throws IOException {
            bookService.downloadBooksImages();
    }

    @Scheduled(fixedRateString = "${job.imageDownloader.failover.fixedRate}")
    public void handleBookImageFailOvers() throws IOException {
        bookService.handleFailover();
    }

}
