package com.jerzyboksa.fishtracker.services.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ImageFileValidatorTest {

  private ImageFileValidator sut;

  private final long MAX_SIZE = 1048576;
  private final long ALLOWED_SIZE = MAX_SIZE / 2;

  @BeforeEach
  void setUp() {
    sut = new ImageFileValidator();
    sut.setMaxSize(MAX_SIZE);
  }

  @ParameterizedTest
  @MethodSource("provideExtensions")
  void is_valid_file_allowed_should_return_true(String fileName, String ext) {
    //given
    MockMultipartFile file = new MockMultipartFile("test", fileName, ext, new byte[(int)(ALLOWED_SIZE)]);
    //when
    var result = sut.isValid(file, null);

    //then
    assertThat(result).isTrue();
  }

  @Test
  void is_valid_wrong_mimetype_should_return_false() {
    //given
    MockMultipartFile file = new MockMultipartFile("file", "test.txt", String.valueOf(MediaType.APPLICATION_JSON), new byte[(int)ALLOWED_SIZE]);
    //when
    boolean result = sut.isValid(file, null);
    //then
    assertThat(result).isFalse();
  }

  @Test
  void is_valid_file_wrong_size_should_return_false() {
    //given
    MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.IMAGE_JPEG_VALUE, new byte[(int)(MAX_SIZE + 1)]);

    //when
    var result = sut.isValid(file, null);

    //then
    assertThat(result).isFalse();
  }

  @Test
  void is_valid_file_null_content_type_should_return_false() {
    //given
    MockMultipartFile file = new MockMultipartFile("file", "test.txt", null, new byte[(int)(ALLOWED_SIZE)]);

    //when
    var result = sut.isValid(file, null);

    //then
    assertThat(result).isFalse();
  }

  private static Stream<Arguments> provideExtensions() {
    return Stream.of(
        Arguments.of("test.jpeg", MediaType.IMAGE_JPEG_VALUE),
        Arguments.of("test.jpg", MediaType.IMAGE_JPEG_VALUE),
        Arguments.of("test.png", MediaType.IMAGE_PNG_VALUE));
  }

}