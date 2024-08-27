package com.jerzyboksa.fishtracker.exceptions;

public class ImageNotDeletedException extends Exception {
  public ImageNotDeletedException(String imageName) {
    super("Can not delete image, imageName=" + imageName);
  }
}
