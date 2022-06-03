package com.app.e_library.persistence.pagination;

import lombok.Setter;

@Setter
public class BookSearchCriteria extends PageRequest {

    private String isbn;
    private String title;
    private Integer publicationYear;
    private String genre;
    private String author;
    private String publisher;


    public String getIsbn() {
        return isbn = isbn == null ? "" : isbn.replaceAll("\\s+", "").toLowerCase();
    }

    public String getTitle() {
        return title = title == null ? "" : title.replaceAll("\\s+", "").toLowerCase();
    }

    public Integer getPublicationYear() {
        return publicationYear = publicationYear == null ? new Integer(0) : publicationYear;
    }

    public String getGenre() {
        return genre = genre == null ? "" : genre.replaceAll("\\s+", "").toLowerCase();
    }

    public String getAuthor() {
        return author = author == null ? "" : author.replaceAll("\\s+", "").toLowerCase();
    }

    public String getPublisher() {
        return publisher = publisher == null ? "" : publisher.replaceAll("\\s+", "").toLowerCase();
    }
}
