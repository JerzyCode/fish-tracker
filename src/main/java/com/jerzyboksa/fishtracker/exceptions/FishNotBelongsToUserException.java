package com.jerzyboksa.fishtracker.exceptions;

public class FishNotBelongsToUserException extends Exception {
  private static final String MESSAGE = "Fish not belongs to user with id=%s.";

  public FishNotBelongsToUserException(Long currentUserId) {
    super(String.format(MESSAGE, currentUserId));
  }
}
