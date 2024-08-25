package com.jerzyboksa.fishtracker.integration;

import com.jerzyboksa.fishtracker.models.User;
import com.jerzyboksa.fishtracker.models.dto.RegisterRequestDTO;
import com.jerzyboksa.fishtracker.repositories.UserRepository;
import com.jerzyboksa.fishtracker.services.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {

  @Autowired
  private AuthService authService;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private TestRestTemplate restTemplate;

  @BeforeEach
  void cleanUp() {
    userRepository.deleteAll();
  }

  @Test
  void register_should_return_ok() {
    //given
    var request = new RegisterRequestDTO("test@mail.com", "test", "password");

    //when
    var response = restTemplate.postForEntity("/auth/register", request, Void.class);

    //then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    var savedUser = userRepository.findByEmail(request.email());
    assertThat(savedUser).isNotEmpty();
    assertThat(savedUser.get().getId()).isNotNull();
    assertThat(savedUser.get().getPassword()).isNotNull();
    assertThat(savedUser.get().getName()).isEqualTo(request.name());
  }

  @Test
  void register_should_return_bad_request_username_taken() {
    //given
    var existingUser = userRepository.save(
        User.builder()
            .name("test")
            .email("test@mail.com")
            .password("test123")
            .build());

    var request = new RegisterRequestDTO(existingUser.getEmail(), "test", "password");

    //when
    var response = restTemplate.postForEntity("/auth/register", request, Void.class);

    //then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  void register_should_return_bad_request_wrong_email_format() {
    //given
    var request = new RegisterRequestDTO("wrongmail.com", "test", "password");

    //when
    var response = restTemplate.postForEntity("/auth/register", request, Void.class);

    //then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }
}
