package com.jerzyboksa.fishtracker.services;

import com.jerzyboksa.fishtracker.TestHelper;
import com.jerzyboksa.fishtracker.exceptions.FishNotBelongsToUserException;
import com.jerzyboksa.fishtracker.models.Fish;
import com.jerzyboksa.fishtracker.models.dto.SaveFishRequestDTO;
import com.jerzyboksa.fishtracker.repositories.FishRepository;
import com.jerzyboksa.fishtracker.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FishServiceTest {

  @Mock
  private UserRepository userRepository;
  @Mock
  private FishRepository fishRepository;

  private FishService sut;

  @BeforeEach
  void setUp() {
    sut = new FishService(userRepository, fishRepository);
  }

  @Test
  void get_fishes_for_user_should_return_fishes_light() {
    //given
    var user1 = TestHelper.createUser(1L, "user1@mail.com", "user1");

    var fish1 = TestHelper.createFish(1L, "Pike", user1);
    var fish2 = TestHelper.createFish(3L, "Perch", user1);

    when(fishRepository.findAllByUserId(user1.getId())).thenReturn(List.of(fish1, fish2));
    //when
    var result = sut.getFishesForUser(user1.getId());

    //then
    assertThat(result).isNotNull();
    assertThat(result).hasSize(2);

    assertThat(result.get(0).getId()).isEqualTo(fish1.getId());
    assertThat(result.get(0).getDate()).isNotNull();
    assertThat(result.get(0).getSpecie()).isEqualTo(fish1.getSpecie());
    assertThat(result.get(0).getSize()).isEqualTo(fish1.getSize());
    assertThat(result.get(0).getWeight()).isEqualTo(fish1.getWeight());

    assertThat(result.get(1).getId()).isEqualTo(fish2.getId());
    assertThat(result.get(1).getDate()).isNotNull();
    assertThat(result.get(1).getSpecie()).isEqualTo(fish2.getSpecie());
    assertThat(result.get(1).getSize()).isEqualTo(fish2.getSize());
    assertThat(result.get(1).getWeight()).isEqualTo(fish2.getWeight());
  }

  @Test
  void create_fish_should_create_fish() {
    //given
    var request = SaveFishRequestDTO.builder()
        .specie("specie")
        .date(LocalDate.now())
        .location("location")
        .build();

    var user = TestHelper.createUser(1L, "test@mail.com", "test");

    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
    when(fishRepository.save(any())).thenReturn(Fish.builder().id(1L).build());
    //when
    var result = sut.createFish(user.getId(), request);

    //then
    assertThat(result).isEqualTo(1L);
    verify(fishRepository, times(1)).save(any());
  }

  @Test
  void update_fish_should_update_fish() throws FishNotBelongsToUserException {
    //given
    var user = TestHelper.createUser(1L, "test@mail.com", "test");
    var fishToUpdate = TestHelper.createFish(1L, "Perch", user);
    var request = SaveFishRequestDTO.builder()
        .date(LocalDate.now())
        .specie("specieUpdate")
        .location("locationUpdated")
        .bait("baitUpdated")
        .build();
    when(fishRepository.findById(fishToUpdate.getId())).thenReturn(Optional.of(fishToUpdate));

    //when
    sut.updateFish(fishToUpdate.getId(), user.getId(), request);

    //then
    verify(fishRepository, times(1)).save(fishToUpdate);
    assertThat(fishToUpdate.getDate()).isEqualTo(request.getDate());
    assertThat(fishToUpdate.getLocation()).isEqualTo(request.getLocation());
    assertThat(fishToUpdate.getBait()).isEqualTo(request.getBait());
  }

  @Test
  void update_fish_should_throw_fish_not_belongs_to_user_exception() {
    //given
    var user = TestHelper.createUser(1L, "test@mail.com", "test");
    var wrongUser = TestHelper.createUser(2L, "wrong@mail.com", "wrongUser");

    var fishToUpdate = TestHelper.createFish(1L, "Perch", user);
    var request = SaveFishRequestDTO.builder()
        .specie("specieUpdate")
        .build();
    when(fishRepository.findById(fishToUpdate.getId())).thenReturn(Optional.of(fishToUpdate));

    //when & then
    assertThrows(FishNotBelongsToUserException.class, () -> sut.updateFish(fishToUpdate.getId(), wrongUser.getId(), request));
  }

  @Test
  void delete_fish_should_delete_fish() throws FishNotBelongsToUserException {
    //given
    var user = TestHelper.createUser(1L, "test@mail.com", "test");
    var fishToDelete = TestHelper.createFish(1L, "Perch", user);

    when(fishRepository.findById(fishToDelete.getId())).thenReturn(Optional.of(fishToDelete));

    //when
    sut.deleteFish(fishToDelete.getId(), user.getId());

    //then
    verify(fishRepository, times(1)).delete(fishToDelete);
  }

  @Test
  void delete_fish_should_throw_fish_not_belongs_to_user_exception() {
    //given
    var user = TestHelper.createUser(1L, "test@mail.com", "test");
    var wrongUser = TestHelper.createUser(2L, "wrong@mail.com", "wrongUser");
    var fishToDelete = TestHelper.createFish(1L, "Perch", user);
    when(fishRepository.findById(fishToDelete.getId())).thenReturn(Optional.of(fishToDelete));

    //when & then
    assertThrows(FishNotBelongsToUserException.class, () -> sut.deleteFish(fishToDelete.getId(), wrongUser.getId()));
  }
}