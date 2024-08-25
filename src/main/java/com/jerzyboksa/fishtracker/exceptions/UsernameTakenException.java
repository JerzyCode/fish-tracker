package com.jerzyboksa.fishtracker.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UsernameTakenException extends Exception {
  private static final String MESSAGE = "Username is already taken.";

  public UsernameTakenException() {
    super(MESSAGE);
  }
}