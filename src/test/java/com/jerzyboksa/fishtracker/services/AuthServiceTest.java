package com.jerzyboksa.fishtracker.services;

import com.jerzyboksa.fishtracker.exceptions.UsernameTakenException;
import com.jerzyboksa.fishtracker.models.User;
import com.jerzyboksa.fishtracker.models.dto.RegisterRequestDTO;
import com.jerzyboksa.fishtracker.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

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
  @Mock
  private JwtService jwtService;

  private AuthService sut;

  @BeforeEach
  void setUp() {
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

}