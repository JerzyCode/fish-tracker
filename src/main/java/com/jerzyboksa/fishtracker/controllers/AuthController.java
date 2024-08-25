package com.jerzyboksa.fishtracker.controllers;

import com.jerzyboksa.fishtracker.exceptions.UsernameTakenException;
import com.jerzyboksa.fishtracker.models.dto.RegisterRequestDTO;
import com.jerzyboksa.fishtracker.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
  private final AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequestDTO request) throws UsernameTakenException {
    log.info("register(), email=" + request.email());
    authService.register(request);
    return ResponseEntity.ok().build();
  }

}
