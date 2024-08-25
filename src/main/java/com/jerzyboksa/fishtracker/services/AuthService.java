package com.jerzyboksa.fishtracker.services;

import com.jerzyboksa.fishtracker.exceptions.UsernameTakenException;
import com.jerzyboksa.fishtracker.models.User;
import com.jerzyboksa.fishtracker.models.dto.RegisterRequestDTO;
import com.jerzyboksa.fishtracker.models.responses.AuthResponse;
import com.jerzyboksa.fishtracker.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final UserRepository repository;
  private final AuthenticationManager authenticationManager;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  public void register(RegisterRequestDTO requestDTO) throws UsernameTakenException {
    Optional<User> existingUser = repository.findByEmail(requestDTO.email());

    if (existingUser.isPresent()) {
      throw new UsernameTakenException();
    }

    String hashedPassword = passwordEncoder.encode(requestDTO.password());
    repository.save(User.builder()
        .name(requestDTO.name())
        .email(requestDTO.email())
        .password(hashedPassword)
        .build()
    );
  }

  private AuthResponse buildAuthenticationResponse(String jwtToken) {
    String username = jwtService.extractUsername(jwtToken);
    Long id = jwtService.extractUserId(jwtToken);
    Long expirationDate = jwtService.extractExpiration(jwtToken).getTime();
    return new AuthResponse(jwtToken, username, id, expirationDate);
  }
}
