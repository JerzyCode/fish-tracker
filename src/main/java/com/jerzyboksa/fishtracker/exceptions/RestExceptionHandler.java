package com.jerzyboksa.fishtracker.exceptions;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler {

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
    log.error("ConstraintViolationException, msg=" + ex.getMessage());
    return createResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(UsernameTakenException.class)
  public ResponseEntity<Object> handleUsernameTakenException(UsernameTakenException ex) {
    log.error("UsernameTakenException, msg=" + ex.getMessage());
    return createResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex) {
    log.error("BadCredentialsException, msg=" + ex.getMessage());
    return createResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  private static ResponseEntity<Object> createResponse(String message, HttpStatus status) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("message", message);
    return new ResponseEntity<>(body, status);
  }
}
