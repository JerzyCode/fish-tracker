package com.jerzyboksa.fishtracker.services;

import com.jerzyboksa.fishtracker.models.dto.CreateFishRequestDTO;
import com.jerzyboksa.fishtracker.models.dto.FishLightDto;
import com.jerzyboksa.fishtracker.repositories.FishRepository;
import com.jerzyboksa.fishtracker.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FishService {
  private final UserRepository userRepository;
  private final FishRepository fishRepository;

  public void createFish(Long userId, CreateFishRequestDTO request) {

  }

  public List<FishLightDto> getFishesForUser(Long userId) {
    return fishRepository.findAllByUserId(userId).stream()
        .map(FishLightDto::of)
        .toList();
  }
}
