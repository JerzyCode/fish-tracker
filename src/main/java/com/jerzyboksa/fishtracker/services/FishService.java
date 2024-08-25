package com.jerzyboksa.fishtracker.services;

import com.jerzyboksa.fishtracker.exceptions.FishNotBelongsToUserException;
import com.jerzyboksa.fishtracker.models.Fish;
import com.jerzyboksa.fishtracker.models.dto.FishLightDto;
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

  public List<FishLightDto> getFishesForUser(Long userId) {
    return fishRepository.findAllByUserId(userId).stream()
        .map(FishLightDto::of)
        .toList();
  }

  public Long createFish(Long userId, SaveFishRequestDTO request) {
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

  public void updateFish(Long fishId, Long userId, SaveFishRequestDTO request) throws FishNotBelongsToUserException {
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
    fishToUpdate.setImgPath(request.getImgPath() == null ? fishToUpdate.getImgPath() : request.getImgPath()); // TODO

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
