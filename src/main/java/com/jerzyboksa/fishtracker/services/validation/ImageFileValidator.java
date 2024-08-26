package com.jerzyboksa.fishtracker.services.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
public class ImageFileValidator implements ConstraintValidator<ValidImage, MultipartFile> {
  private long maxSize;
  private static final String TOO_BIG_FILE = "Rozmiar pliku jest zbyt duży";
  private static final String ATTACHMENTS_DISABLED = "Załączniki są wyłączone";
  private static final String BAD_EXTENSION_FILE = "Plik posiada niepoprawne rozszerzenie";
  private static final String INVALID_FILE_NAME = "Niewłaściwa nazwa załącznika";

  @Override
  public void initialize(ValidImage constraintAnnotation) {
    this.maxSize = constraintAnnotation.maxSize();
  }

  @Override
  public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
    String fileName = file.getOriginalFilename();
    String contentType = file.getContentType();

    if (fileName == null || contentType == null) {
      return false;
    }

    return isValidSize(file.getSize()) && isSupportedMimeTypeAndExtension(contentType, fileName);
  }

  private boolean isValidSize(long numOfBytes) {
    if (numOfBytes > maxSize) {
      log.error(TOO_BIG_FILE);
      return false;
    }
    return true;
  }

  private boolean isSupportedMimeTypeAndExtension(String mimeType, String fileName) {
    String extension = getExtension(fileName);

    if (mimeType.contains("image/jpeg") && extension.equals(".jpeg"))
      return true;

    if (mimeType.contains("image/jpeg") && extension.equals(".jpg"))
      return true;

    if (mimeType.contains("image/png") && extension.equals(".png"))
      return true;

    log.error(BAD_EXTENSION_FILE);
    return false;
  }

  public void setMaxSize(long maxSize) {
    this.maxSize = maxSize;
  }

  private static String getExtension(String fileName) {
    return fileName.substring(fileName.lastIndexOf("."));
  }

}
