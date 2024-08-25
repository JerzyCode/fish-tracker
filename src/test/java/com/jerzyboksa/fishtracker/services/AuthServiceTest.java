package com.jerzyboksa.fishtracker.services;

import com.jerzyboksa.fishtracker.exceptions.UsernameTakenException;
import com.jerzyboksa.fishtracker.models.User;
import com.jerzyboksa.fishtracker.models.dto.LoginRequestDTO;
import com.jerzyboksa.fishtracker.models.dto.RegisterRequestDTO;
import com.jerzyboksa.fishtracker.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
  @Mock
  private UserRepository userRepository;
  @Mock
  private AuthenticationManager authManager;
  @Mock
  private PasswordEncoder passwordEncoder;

  private AuthService sut;

  @BeforeEach
  void setUp() {
    JwtService jwtService = new JwtService();
    jwtService.setSecretKey("2afbfac8fe49058f12874b6c7f92d6c842d2ec05f621f9b3b4ba437eb8f7b338");
    jwtService.setJwtExpiration(31536000000L);
    sut = new AuthService(userRepository, authManager, passwordEncoder, jwtService);
  }

  @Test
  void register_should_throw_username_taken_exception() {
    //given
    var existingUser = User.builder()
        .id(1L)
        .email("test@mail.com")
        .name("test")
        .build();
    when(userRepository.findByEmail(existingUser.getEmail())).thenReturn(Optional.of(existingUser));

    var request = new RegisterRequestDTO(existingUser.getEmail(), "name", "test123");

    //when & then
    assertThrows(UsernameTakenException.class, () -> sut.register(request));
    verify(userRepository, times(0)).save(any());
  }

  @Test
  void register_should_success() throws UsernameTakenException {
    //given
    var request = new RegisterRequestDTO("test@mail.com", "name", "test123");

    //when
    sut.register(request);

    //then
    verify(userRepository, times(1)).save(any());
  }

  @Test
  void login_should_throw_bad_credentials_exception() {
    //given
    var request = new LoginRequestDTO("test@mail.com", "password");
    when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());

    //when & then
    assertThrows(BadCredentialsException.class, () -> sut.login(request));
  }

  @Test
  void login_should_success() {
    //given
    var request = new LoginRequestDTO("test@mail.com", "password");
    var savedUser = User.builder()
        .id(1L)
        .email(request.email())
        .password(request.password())
        .build();

    when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(savedUser));

    //when
    var response = sut.login(request);

    //then
    assertThat(response.token()).isNotNull();
    assertThat(response.userId()).isNotNull();
    assertThat(response.expirationDate()).isNotNull();
    assertThat(response.username()).isEqualTo(savedUser.getEmail());
    verify(authManager, times(1)).authenticate(any());
  }

}