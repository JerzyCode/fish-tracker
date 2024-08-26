package com.jerzyboksa.fishtracker.exceptions;

public class ImageSaveFailException extends Exception {
  public ImageSaveFailException() {
    super("Failed to save image.");
  }
}
