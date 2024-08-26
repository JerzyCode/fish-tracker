package com.jerzyboksa.fishtracker.exceptions;

public class ImageNotFoundException extends Exception {
  public ImageNotFoundException(String imageName) {
    super("Failed to find image, imageName=" + imageName);
  }
}
