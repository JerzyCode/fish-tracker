package com.jerzyboksa.fishtracker.controllers;

import com.jerzyboksa.fishtracker.models.dto.FishLightDto;
import com.jerzyboksa.fishtracker.services.FishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/api/fish")
@Slf4j
@RequiredArgsConstructor
public class FishController {
  private final FishService fishService;

  @GetMapping
  public ResponseEntity<List<FishLightDto>> getFishesLightForUser(@RequestParam Long userId) {
    return ResponseEntity.ok(fishService.getFishesForUser(userId));
  }
}
