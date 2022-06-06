package com.app.e_library.service.dto;

import com.app.e_library.persistence.entity.BookImageEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;


@Data
@Builder
@JsonInclude(NON_NULL)
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

        return BookImageDto
                .builder()
                .id(bookImageEntity.getId())
                .imageDownloadStatus(bookImageEntity.getImageDownloadStatus())
                .imageDownloadStartTime(bookImageEntity.getImageDownloadStartTime())
                .imageURLSmall(bookImageEntity.getImageURLSmall())
                .imageURLMedium(bookImageEntity.getImageURLMedium())
                .imageURLLarge(bookImageEntity.getImageURLLarge())
                .coverImagePath(bookImageEntity.getCoverImagePath())
                .thumbnailPath(bookImageEntity.getThumbnailPath())
                .type(bookImageEntity.getType())
                .coverImageSizeBytes(bookImageEntity.getCoverImageSizeBytes())
                .thumbnailSizeBytes(bookImageEntity.getThumbnailSizeBytes())
                .build();
    }


    public static BookImageEntity mapToEntity(BookImageDto bookImageDto){

        return BookImageEntity
                .builder()
                .id(bookImageDto.getId())
                .imageDownloadStatus(bookImageDto.getImageDownloadStatus())
                .imageDownloadStartTime(bookImageDto.getImageDownloadStartTime())
                .imageURLSmall(bookImageDto.getImageURLSmall())
                .imageURLMedium(bookImageDto.getImageURLMedium())
                .imageURLLarge(bookImageDto.getImageURLLarge())
                .coverImagePath(bookImageDto.getCoverImagePath())
                .thumbnailPath(bookImageDto.getThumbnailPath())
                .type(bookImageDto.getType())
                .coverImageSizeBytes(bookImageDto.getCoverImageSizeBytes())
                .thumbnailSizeBytes(bookImageDto.getThumbnailSizeBytes())
                .build();

    }

    public static List<BookImageDto> mapToDtoList(List<BookImageEntity> bookImageEntities) {
        return bookImageEntities.stream().map(BookImageDto::mapToDto).collect(Collectors.toList());
    }

    public static List<BookImageEntity> mapToEntityList(List<BookImageDto> bookImageDtos){
        return bookImageDtos.stream().map(BookImageDto::mapToEntity).collect(Collectors.toList());
    }

}
