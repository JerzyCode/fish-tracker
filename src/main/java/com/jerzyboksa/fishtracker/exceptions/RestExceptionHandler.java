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
import java.util.NoSuchElementException;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler {

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
    log.error("ConstraintViolationException, msg=" + ex.getMessage());
    return createResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException ex) {
    log.error("NoSuchElementException, msg=" + ex.getMessage());
    return createResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ImageNotFoundException.class)
  public ResponseEntity<Object> handleImageNotFoundException(ImageNotFoundException ex) {
    log.error("ImageNotFoundException, msg=" + ex.getMessage());
    return createResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ImageSaveFailException.class)
  public ResponseEntity<Object> handleImageSaveFailException(ImageSaveFailException ex) {
    log.error("handleImageSaveFailException, msg=" + ex.getMessage());
    return createResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ImageNotDeletedException.class)
  public ResponseEntity<Object> handleImageNotDeletedException(ImageNotDeletedException ex) {
    log.error("handleImageNotDeletedException, msg=" + ex.getMessage());
    return createResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(FishNotBelongsToUserException.class)
  public ResponseEntity<Object> handleFishNotBelongsToUserException(FishNotBelongsToUserException ex) {
    log.error("FishNotBelongsToUserException, msg=" + ex.getMessage());
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
