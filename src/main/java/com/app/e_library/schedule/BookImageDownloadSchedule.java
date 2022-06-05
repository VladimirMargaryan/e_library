package com.app.e_library.schedule;

import com.app.e_library.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@AllArgsConstructor
@Component
public class BookImageDownloadSchedule {

    private final BookService bookService;

    @Async
    @Scheduled(fixedRateString = "${job.imageDownloader.fixedRate}")
    public void downloadBookImages() throws IOException {
        bookService.downloadBooksImages();
    }

    @Scheduled(fixedRateString = "${job.imageDownloader.failover.fixedRate}")
    public void handleBookImageFailOvers() throws IOException {
        bookService.handleFailover();
    }

}
