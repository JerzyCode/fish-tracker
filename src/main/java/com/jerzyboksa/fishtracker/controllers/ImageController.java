package com.jerzyboksa.fishtracker.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/api/image")
@RequiredArgsConstructor
@Slf4j
public class ImageController {

  @GetMapping("/img")
  public ResponseEntity<Resource> getImage() {
    log.info("getImage()");
    try {
      Resource resource = new ClassPathResource("static/assets/image_not_found.jpg");
      if (!resource.exists()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
      }
      String contentType = "image/jpeg";
      return ResponseEntity.ok()
          .contentType(MediaType.parseMediaType(contentType))
          .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=image_not_foun.jpg")
          .body(resource);
    }
    catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

}
