package com.app.e_library.service.dto;

import com.app.e_library.persistence.entity.BookImageEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookImageDto {

    private Long id;

    private BookImageDownloadStatus imageDownloadStatus;
    private Long imageDownloadStartTime;
    private String imageURLSmall;
    private String imageURLMedium;
    private String imageURLLarge;
    private String coverImagePath;
    private String thumbnailPath;
    private String type;

    @Valid
    private Long coverImageSizeBytes;

    @Valid
    private Long thumbnailSizeBytes;


    public static BookImageDto mapToDto(BookImageEntity bookImageEntity){
        BookImageDto bookImageDto = new BookImageDto();
        bookImageDto.setId(bookImageEntity.getId());
        bookImageDto.setImageDownloadStatus(bookImageEntity.getImageDownloadStatus());
        bookImageDto.setImageDownloadStartTime(bookImageEntity.getImageDownloadStartTime());
        bookImageDto.setImageURLSmall(bookImageEntity.getImageURLSmall());
        bookImageDto.setImageURLMedium(bookImageEntity.getImageURLMedium());
        bookImageDto.setImageURLLarge(bookImageEntity.getImageURLLarge());
        bookImageDto.setCoverImagePath(bookImageEntity.getCoverImagePath());
        bookImageDto.setThumbnailPath(bookImageEntity.getThumbnailPath());
        bookImageDto.setType(bookImageEntity.getType());
        bookImageDto.setCoverImageSizeBytes(bookImageEntity.getCoverImageSizeBytes());
        bookImageDto.setThumbnailSizeBytes(bookImageEntity.getThumbnailSizeBytes());

        return bookImageDto;
    }


    public static BookImageEntity mapToEntity(BookImageDto bookImageDto){
        BookImageEntity bookImageEntity = new BookImageEntity();
        bookImageEntity.setId(bookImageDto.getId());
        bookImageEntity.setImageDownloadStatus(bookImageDto.getImageDownloadStatus());
        bookImageEntity.setImageDownloadStartTime(bookImageDto.getImageDownloadStartTime());
        bookImageEntity.setImageURLSmall(bookImageDto.getImageURLSmall());
        bookImageEntity.setImageURLMedium(bookImageDto.getImageURLMedium());
        bookImageEntity.setImageURLLarge(bookImageDto.getImageURLLarge());
        bookImageEntity.setCoverImagePath(bookImageDto.getCoverImagePath());
        bookImageEntity.setThumbnailPath(bookImageDto.getThumbnailPath());
        bookImageEntity.setType(bookImageDto.getType());
        bookImageEntity.setCoverImageSizeBytes(bookImageDto.getCoverImageSizeBytes());
        bookImageEntity.setThumbnailSizeBytes(bookImageDto.getThumbnailSizeBytes());

        return bookImageEntity;
    }

    public static List<BookImageDto> mapToDtoList(List<BookImageEntity> bookImageEntities) {
        return bookImageEntities.stream().map(BookImageDto::mapToDto).collect(Collectors.toList());
    }

    public static List<BookImageEntity> mapToEntityList(List<BookImageDto> bookImageDtos){
        return bookImageDtos.stream().map(BookImageDto::mapToEntity).collect(Collectors.toList());
    }

}
