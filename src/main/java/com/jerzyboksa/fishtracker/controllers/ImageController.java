package com.jerzyboksa.fishtracker.controllers;

import com.jerzyboksa.fishtracker.exceptions.ImageNotFoundException;
import com.jerzyboksa.fishtracker.services.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/api/fishImage")
@RequiredArgsConstructor
@Slf4j
public class ImageController {

  private final ImageService imageService;

  @GetMapping("/{imageName}")
  public ResponseEntity<Resource> getFishImage(@PathVariable String imageName) throws ImageNotFoundException {
    log.info("getFishImage(), imageName=" + imageName);
    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType("image/jpeg"))
        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=image_not_foun.jpg")
        .body(imageService.getImage(imageName));
  }

}
