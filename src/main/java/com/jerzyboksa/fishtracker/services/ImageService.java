package com.jerzyboksa.fishtracker.services;

import com.jerzyboksa.fishtracker.exceptions.ImageNotFoundException;
import com.jerzyboksa.fishtracker.exceptions.ImageSaveFailException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Instant;

@Service
public class ImageService {

  private static final String IMAGE_BASE_PATH = new File("src/main/resources/static/assets").getAbsolutePath();

  public String saveImage(MultipartFile image) throws ImageSaveFailException {
    try {
      String originalFilename = image.getOriginalFilename();
      String extension = originalFilename != null && originalFilename.contains(".")
          ? originalFilename.substring(originalFilename.lastIndexOf(".") + 1)
          : "jpg";

      String imageName = generateImageName();
      String path = IMAGE_BASE_PATH + File.separator + imageName + "." + extension;

      File destination = new File(path);
      if (!destination.getParentFile().exists()) {
        destination.getParentFile().mkdirs();
      }

      image.transferTo(destination);

      return path;
    }
    catch (IOException ex) {
      throw new ImageSaveFailException();
    }
  }

  public Resource getImage(String imageName) throws ImageNotFoundException {
    try {
      ClassPathResource resource = new ClassPathResource("static/assets/" + imageName);
      if (!resource.exists()) {
        throw new ImageNotFoundException(imageName);
      }
      return resource;
    }
    catch (Exception e) {
      throw new ImageNotFoundException(imageName);
    }
  }

  private String generateImageName() {
    return String.valueOf(Instant.now().toEpochMilli());
  }

}
