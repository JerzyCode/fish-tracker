package com.jerzyboksa.fishtracker.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoAuthHeaderException extends Exception {
  private static final String MESSAGE = "No authorization header included.";

  public NoAuthHeaderException() {
    super(MESSAGE);
  }
}