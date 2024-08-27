package com.jerzyboksa.fishtracker.services;

import com.jerzyboksa.fishtracker.exceptions.UsernameTakenException;
import com.jerzyboksa.fishtracker.models.User;
import com.jerzyboksa.fishtracker.models.dto.AuthResponseDTO;
import com.jerzyboksa.fishtracker.models.dto.LoginRequestDTO;
import com.jerzyboksa.fishtracker.models.dto.RegisterRequestDTO;
import com.jerzyboksa.fishtracker.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

  public AuthResponseDTO login(LoginRequestDTO requestDTO) {
    var user = repository.findByEmail(requestDTO.email()).orElseThrow(() -> new BadCredentialsException("Bad Credentials"));
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestDTO.email(), requestDTO.password()));
    var jwtToken = jwtService.buildToken(user);

    return new AuthResponseDTO(jwtToken,
        user.getName(),
        jwtService.extractUserId(jwtToken),
        jwtService.extractExpiration(jwtToken).getTime());
  }

}
