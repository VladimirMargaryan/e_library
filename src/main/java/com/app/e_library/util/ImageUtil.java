package com.app.e_library.util;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.validation.constraints.NotNull;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static java.awt.Image.SCALE_SMOOTH;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static java.net.HttpURLConnection.HTTP_OK;

@Component
public class ImageUtil {

    public byte[] getImageByteArray(@NotNull URL imageUrl,
                                    @NotNull String extension){

       if (!Arrays.asList("jpg", "jpeg").contains(extension.toLowerCase()))
           return null;

        byte[] imageByteArray = new byte[0];
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(imageUrl.openStream())) {
            imageByteArray = IOUtils.toByteArray(bufferedInputStream);

            byte firstByte = (byte) 0xFF;
            byte secondByte = (byte) 0xD8;

            if (imageByteArray[0] != firstByte || imageByteArray[1] != secondByte){
                BufferedImage image = ImageIO.read( new ByteArrayInputStream( imageByteArray ));
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write( image, extension, byteArrayOutputStream );
                imageByteArray = byteArrayOutputStream.toByteArray();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageByteArray;
    }

    public byte[] getImageByteArray(@NotNull URL imageUrl){
        return getImageByteArray(imageUrl, "jpg");
    }

    public Path saveImageLocally(@NotNull Path directory,
                                 @NotNull String filename,
                                 @NotNull URL imageUrl,
                                 @NotNull byte[] imageByteArray){

        Path imagePath = createImagePath(directory, filename);
        try (FileOutputStream outputStream = new FileOutputStream(imagePath.toString())) {

            HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();

            if (connection.getResponseCode() != HTTP_OK || !connection.getContentType().equals("image/jpeg")) {
                return null;
            }
            outputStream.write(imageByteArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imagePath;
    }

    public Path createThumbnailImage(@NotNull BufferedImage image,
                                      @NotNull Path directory,
                                      @NotNull String filename,
                                      @NotNull String imageExtension) throws IOException {

        Path thumbnailPath = createImagePath(directory, filename);

        int width = 128;
        int height = 128;

        double outputAspect = 1.0 * width / height;
        double inputAspect = 1.0 * image.getWidth() / image.getHeight();

        if (outputAspect < inputAspect)
            height = (int) (width / inputAspect);
        else
            width = (int) (height * inputAspect);

        BufferedImage thumbnail = new BufferedImage(width, height, TYPE_INT_RGB);
        thumbnail.createGraphics().drawImage(image.getScaledInstance(width, height, SCALE_SMOOTH), 0, 0, null);
        ImageIO.write(thumbnail, imageExtension, thumbnailPath.toFile());

        return thumbnailPath;
    }

    private Path createImagePath(Path directory, String filename){
        return Paths.get(directory + File.separator + filename + ".jpg");
    }

}
