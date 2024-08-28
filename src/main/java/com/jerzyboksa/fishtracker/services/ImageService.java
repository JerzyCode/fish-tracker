package com.jerzyboksa.fishtracker.services;

import com.jerzyboksa.fishtracker.exceptions.ImageNotDeletedException;
import com.jerzyboksa.fishtracker.exceptions.ImageNotFoundException;
import com.jerzyboksa.fishtracker.exceptions.ImageSaveFailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Instant;

@Service
@Slf4j
public class ImageService {

  @Value("${custom.image_path}")
  private String imgPath;

  private String systemPath = System.getProperty("user.dir") + File.separator;

  private static final String IMAGE_NOT_FOUND_JPG = "image_not_found.jpg";

  public String saveImage(MultipartFile image) throws ImageSaveFailException {
    try {
      if (image == null || image.isEmpty()) {
        return IMAGE_NOT_FOUND_JPG;
      }

      String originalFilename = image.getOriginalFilename();
      String extension = originalFilename != null && originalFilename.contains(".")
          ? originalFilename.substring(originalFilename.lastIndexOf(".") + 1)
          : "jpg";

      String imageName = generateImageName();
      String path = systemPath + imgPath + File.separator + imageName + "." + extension;

      log.debug("saveImage() path=" + path);

      File destination = new File(path);
      if (!destination.getParentFile().exists()) {

        log.debug("saveImage(), path no exist, creating path");
        var result = destination.getParentFile().mkdirs();

        log.debug("saveImage(), isPathCreated=" + result);
      }

      image.transferTo(destination);

      return imageName + "." + extension;
    }
    catch (IOException ex) {
      log.error("Fail saving image!!, Stacktrace:");
      ex.printStackTrace();
      throw new ImageSaveFailException();
    }
  }

  public Resource getImage(String imageName) throws ImageNotFoundException {
    try {
      File file = new File(systemPath + imgPath + File.separator + imageName);

      if (!file.exists()) {
        log.debug("getImage(), image not found, so return image_not_found.jpg");
        return new ClassPathResource("static/assets/image_not_found.jpg");
        //        log.error("Image not found, path=" + file.getAbsolutePath());
        //        throw new ImageNotFoundException(imageName);
      }

      return new FileSystemResource(file);
    }
    catch (Exception e) {
      log.error("Error occurred while retrieving image, imageName=" + imageName, e);
      throw new ImageNotFoundException(imageName);
    }
  }

  public void deleteImage(String imageName) throws ImageNotDeletedException, ImageNotFoundException {
    if (imageName.equals(IMAGE_NOT_FOUND_JPG)) {
      return;
    }
    String path = systemPath + imgPath + File.separator + imageName;

    File file = new File(path);
    if (file.exists()) {
      var isDeleted = file.delete();
      if (!isDeleted) {
        log.error("Delete failed, deletePath=" + path);
        throw new ImageNotDeletedException(imageName);
      }
    }
    else {
      log.error("Image not exist, path=" + path);
      throw new ImageNotFoundException(imageName);
    }

  }

  private String generateImageName() {
    return String.valueOf(Instant.now().toEpochMilli());
  }

}
