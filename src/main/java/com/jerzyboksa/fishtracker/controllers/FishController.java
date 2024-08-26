package com.jerzyboksa.fishtracker.controllers;

import com.jerzyboksa.fishtracker.exceptions.FishNotBelongsToUserException;
import com.jerzyboksa.fishtracker.exceptions.ImageSaveFailException;
import com.jerzyboksa.fishtracker.exceptions.NoAuthHeaderException;
import com.jerzyboksa.fishtracker.models.dto.FishDetailsDTO;
import com.jerzyboksa.fishtracker.models.dto.FishLightDto;
import com.jerzyboksa.fishtracker.models.dto.SaveFishRequestDTO;
import com.jerzyboksa.fishtracker.services.FishService;
import com.jerzyboksa.fishtracker.services.ImageService;
import com.jerzyboksa.fishtracker.services.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/rest/api/fish")
@Slf4j
@RequiredArgsConstructor
public class FishController {
  private final FishService fishService;
  private final JwtService jwtService;
  private final ImageService imageService;
  private final NativeWebRequest nativeWebRequest;

  @GetMapping
  public ResponseEntity<List<FishLightDto>> getFishesLightForUser(@RequestParam Long userId) {
    log.debug("getFishesLightForUser(), userId=" + userId);
    return ResponseEntity.ok(fishService.getFishesForUser(userId));
  }

  @GetMapping("/{fishId}")
  public ResponseEntity<FishDetailsDTO> getFishDetails(@PathVariable Long fishId) {
    log.debug("getFishDetails(), fishId=" + fishId);
    return ResponseEntity.ok(fishService.getFishDetails(fishId));
  }

  @PostMapping //TODO image validation jako adnotacja
  public ResponseEntity<Long> createFish(@ModelAttribute SaveFishRequestDTO request, @RequestParam MultipartFile image)
      throws NoAuthHeaderException, ImageSaveFailException {
    var userId = getUserId();
    log.debug(String.format("createFish(), userId=%d, request=%s", userId, request.toString()));
    var imgName = imageService.saveImage(image);

    return ResponseEntity.ok(fishService.createFish(userId, request, imgName));
  }

  @PutMapping
  public ResponseEntity<Void> updateFish(@RequestParam Long fishId, @RequestBody SaveFishRequestDTO request)
      throws NoAuthHeaderException, FishNotBelongsToUserException {
    log.debug(String.format("updateFish(), fishId=%d, request=%s", fishId, request.toString()));
    fishService.updateFish(fishId, getUserId(), request);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping
  public ResponseEntity<Void> deleteFish(@RequestParam Long fishId)
      throws NoAuthHeaderException, FishNotBelongsToUserException {
    log.debug(String.format("delete(), fishId=%d", fishId));
    fishService.deleteFish(fishId, getUserId());
    return ResponseEntity.ok().build();
  }

  private Long getUserId() throws NoAuthHeaderException {
    var bearerPrefix = "Bearer ";
    var header = nativeWebRequest.getHeader("Authorization");
    if (header == null) {
      throw new NoAuthHeaderException();
    }
    var token = header.contains(bearerPrefix) ? header.substring(bearerPrefix.length()) : header;
    return jwtService.extractUserId(token);
  }

}
