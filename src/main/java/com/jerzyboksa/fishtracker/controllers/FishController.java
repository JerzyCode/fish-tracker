package com.jerzyboksa.fishtracker.controllers;

import com.jerzyboksa.fishtracker.exceptions.FishNotBelongsToUserException;
import com.jerzyboksa.fishtracker.exceptions.NoAuthHeaderException;
import com.jerzyboksa.fishtracker.models.dto.FishLightDto;
import com.jerzyboksa.fishtracker.models.dto.SaveFishRequestDTO;
import com.jerzyboksa.fishtracker.services.FishService;
import com.jerzyboksa.fishtracker.services.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;

@RestController
@RequestMapping("/rest/api/fish")
@Slf4j
@RequiredArgsConstructor
public class FishController {
  private final FishService fishService;
  private final JwtService jwtService;
  private final NativeWebRequest nativeWebRequest;

  @GetMapping
  public ResponseEntity<List<FishLightDto>> getFishesLightForUser(@RequestParam Long userId) {
    log.debug("getFishesLightForUser(), userId=" + userId);
    return ResponseEntity.ok(fishService.getFishesForUser(userId));
  }

  @PostMapping
  public ResponseEntity<Long> createFish(@RequestBody SaveFishRequestDTO request) throws NoAuthHeaderException {
    var userId = getUserId();
    log.debug(String.format("createFish(), userId=%d, request=%s", userId, request.toString()));
    return ResponseEntity.ok(fishService.createFish(userId, request));
  }

  @PutMapping
  public ResponseEntity<Void> updateFish(@RequestParam Long fishId, @RequestBody SaveFishRequestDTO request)
      throws NoAuthHeaderException, FishNotBelongsToUserException {
    log.debug(String.format("updateFish(), fishId=%d, request=%s", fishId, request.toString()));
    fishService.updateFish(fishId, getUserId(), request);
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
