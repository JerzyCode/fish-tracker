package com.jerzyboksa.fishtracker.services;

import com.jerzyboksa.fishtracker.exceptions.ImageSaveFailException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

  private final String TEST_IMG_PATH = System.getProperty("user.dir") + File.separator + "assets";
  private static final String IMAGE_NOT_FOUND_JPG = "image_not_found.jpg";

  @Mock
  private MultipartFile image;
  private ImageService sut;

  @BeforeEach
  void setUp() {
    sut = new ImageService();
    sut.setImgPath("/assets");
  }

  @Test
  void save_empty_image_and_return_image_name_should_return_image_not_found_name() throws ImageSaveFailException {
    //given
    when(image.isEmpty()).thenReturn(true);

    //when
    var result = sut.saveImageAndReturnImageName(image);

    //then
    assertThat(result).isEqualTo(IMAGE_NOT_FOUND_JPG);
  }

  @Test
  void save_null_image_and_return_image_name_should_return_image_not_found_name() throws ImageSaveFailException {
    //when
    var result = sut.saveImageAndReturnImageName(null);

    //then
    assertThat(result).isEqualTo(IMAGE_NOT_FOUND_JPG);
  }

  @Test
  void save_image_should_save_image() throws ImageSaveFailException, IOException {
    //given
    when(image.isEmpty()).thenReturn(false);
    when(image.getOriginalFilename()).thenReturn("testImage.png");
    doNothing().when(image).transferTo(any(File.class));

    //when
    var result = sut.saveImageAndReturnImageName(image);

    //then
    ArgumentCaptor<File> destination = ArgumentCaptor.forClass(File.class);
    verify(image, times(1)).transferTo(destination.capture());

    File capturedDestination = destination.getValue();
    String path = capturedDestination.getPath();

    assertThat(result).isEqualTo(path.substring(TEST_IMG_PATH.length() + 1));
    assertThat(path).contains(TEST_IMG_PATH);
  }

  @Test
  void save_image_should_throw_image_save_fail_exception() throws IOException {
    //given
    when(image.isEmpty()).thenReturn(false);
    when(image.getOriginalFilename()).thenReturn("testImage.png");
    doThrow(IOException.class).when(image).transferTo(any(File.class));

    //when && then
    assertThrows(ImageSaveFailException.class, () -> sut.saveImageAndReturnImageName(image));
  }

}