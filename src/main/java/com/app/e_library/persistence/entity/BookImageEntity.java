package com.app.e_library.persistence.entity;

import com.app.e_library.service.dto.BookImageDownloadStatus;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.Objects;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "book_image")
public class BookImageEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "image_downlad_status")
    @Enumerated(STRING)
    private BookImageDownloadStatus imageDownloadStatus;

    @Column(name = "image_download_start_time")
    private Long imageDownloadStartTime;

    @URL(message = "URL is not valid")
    @Column(name = "image_url_S")
    private String imageURLSmall;

    @URL(message = "URL is not valid")
    @Column(name = "image_url_m")
    private String imageURLMedium;

    @URL(message = "URL is not valid")
    @Column(name = "image_url_l")
    private String imageURLLarge;

    @Column(name = "cover_image_path")
    private String coverImagePath;

    @Column(name = "thumbnail_path")
    private String thumbnailPath;

    @Column(name = "type")
    private String type;

    @Column(name = "cover_image_size_bytes")
    @Valid
    private Long coverImageSizeBytes;

    @Column(name = "thumbnail_size_bytes")
    @Valid
    private Long thumbnailSizeBytes;

    @OneToOne(mappedBy = "bookImage", fetch = LAZY)
    @ToString.Exclude
    private BookEntity book;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BookImageEntity that = (BookImageEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
