package com.jerzyboksa.fishtracker.services;

import com.jerzyboksa.fishtracker.models.Fish;
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

  public List<FishLightDto> getFishesForUser(Long userId) {
    return fishRepository.findAllByUserId(userId).stream()
        .map(FishLightDto::of)
        .toList();
  }

  public Long createFish(Long userId, CreateFishRequestDTO request) {
    var user = userRepository.findById(userId).orElseThrow();
    var fish = Fish.builder()
        .date(request.getDate())
        .specie(request.getSpecie())
        .size(request.getSize())
        .weight(request.getWeight())
        .location(request.getLocation())
        .method(request.getMethod())
        .bait(request.getBait())
        .imgPath("") //TODO save picture and create path
        .user(user)
        .build();

    return fishRepository.save(fish).getId();
  }
}
