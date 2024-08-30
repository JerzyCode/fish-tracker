package com.jerzyboksa.fishtracker.services;

import com.jerzyboksa.fishtracker.exceptions.FishNotBelongsToUserException;
import com.jerzyboksa.fishtracker.models.Fish;
import com.jerzyboksa.fishtracker.models.dto.FishDetailsDTO;
import com.jerzyboksa.fishtracker.models.dto.FishLightDTO;
import com.jerzyboksa.fishtracker.models.dto.SaveFishRequestDTO;
import com.jerzyboksa.fishtracker.repositories.FishRepository;
import com.jerzyboksa.fishtracker.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FishService {
  private final UserRepository userRepository;
  private final FishRepository fishRepository;

  public List<FishLightDTO> getFishesForUser(Long userId) {
    return fishRepository.findAllByUserId(userId).stream()
        .map(FishLightDTO::of)
        .toList();
  }

  public FishDetailsDTO getFishDetails(Long fishId) {
    var fish = fishRepository.findById(fishId).orElseThrow();
    var user = fish.getUser();

    return FishDetailsDTO.builder()
        .id(fish.getId())
        .date(fish.getDate())
        .specie(fish.getSpecie())
        .size(fish.getSize())
        .weight(fish.getWeight())
        .location(fish.getLocation())
        .method(fish.getMethod())
        .bait(fish.getBait())
        .userId(user.getId())
        .username(user.getName())
        .build();
  }

  public FishLightDTO getRandomFish() {
    var randomFish = fishRepository.getRandomFish().orElseThrow();
    return FishLightDTO.of(randomFish);
  }

  public String getFishImageName(Long fishId) {
    var fish = fishRepository.findById(fishId).orElseThrow();
    return fish.getImageName();
  }

  public Long createFish(Long userId, SaveFishRequestDTO request, String imageName) {
    var user = userRepository.findById(userId).orElseThrow();
    var fish = Fish.builder()
        .date(request.getDate())
        .specie(request.getSpecie())
        .size(request.getSize())
        .weight(request.getWeight())
        .location(request.getLocation())
        .method(request.getMethod())
        .bait(request.getBait())
        .imageName(imageName)
        .user(user)
        .build();

    return fishRepository.save(fish).getId();
  }

  public void updateFish(Long fishId, Long userId, SaveFishRequestDTO request, String imageName) throws FishNotBelongsToUserException {
    var fishToUpdate = fishRepository.findById(fishId).orElseThrow();

    if (doesFishNotBelongToUser(fishToUpdate, userId)) {
      throw new FishNotBelongsToUserException(userId);
    }

    fishToUpdate.setDate(request.getDate() == null ? fishToUpdate.getDate() : request.getDate());
    fishToUpdate.setSpecie(request.getSpecie() == null ? fishToUpdate.getSpecie() : request.getSpecie());
    fishToUpdate.setSize(request.getSize() == null ? fishToUpdate.getSize() : request.getSize());
    fishToUpdate.setWeight(request.getWeight() == null ? fishToUpdate.getWeight() : request.getWeight());
    fishToUpdate.setLocation(request.getLocation() == null ? fishToUpdate.getLocation() : request.getLocation());
    fishToUpdate.setMethod(request.getMethod() == null ? fishToUpdate.getMethod() : request.getMethod());
    fishToUpdate.setBait(request.getBait() == null ? fishToUpdate.getBait() : request.getBait());
    fishToUpdate.setImageName(imageName == null ? fishToUpdate.getImageName() : imageName);

    fishRepository.save(fishToUpdate);
  }

  public void deleteFish(Long fishId, Long userId) throws FishNotBelongsToUserException {
    var fishToDelete = fishRepository.findById(fishId).orElseThrow();

    if (doesFishNotBelongToUser(fishToDelete, userId)) {
      throw new FishNotBelongsToUserException(userId);
    }

    fishRepository.delete(fishToDelete);
  }

  private boolean doesFishNotBelongToUser(Fish fish, Long userId) {
    var fishUser = fish.getUser();
    return !Objects.equals(fishUser.getId(), userId);
  }

}
