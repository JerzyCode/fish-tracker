package com.jerzyboksa.fishtracker.integration;

import com.jerzyboksa.fishtracker.models.User;
import com.jerzyboksa.fishtracker.models.dto.LoginRequestDTO;
import com.jerzyboksa.fishtracker.models.dto.RegisterRequestDTO;
import com.jerzyboksa.fishtracker.models.dto.AuthResponseDTO;
import com.jerzyboksa.fishtracker.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {
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

  @Test
  void login_should_return_ok() {
    //given
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    String password = "password";
    var existingUser = userRepository.save(
        User.builder()
            .name("test")
            .email("test@mail.com")
            .password(encoder.encode(password))
            .build());

    var request = new LoginRequestDTO(existingUser.getEmail(), password);

    //when
    var response = restTemplate.postForEntity("/auth/login", request, AuthResponseDTO.class);

    //then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().email()).isEqualTo(existingUser.getEmail());
    assertThat(response.getBody().token()).isNotNull();
  }

  @Test
  void login_should_return_bad_request_wrong_credentials() {
    //given

    var request = new LoginRequestDTO("test@mail.com", "password");

    //when
    var response = restTemplate.postForEntity("/auth/login", request, AuthResponseDTO.class);

    //then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

}
