package com.jerzyboksa.fishtracker.services;

import com.jerzyboksa.fishtracker.TestHelper;
import com.jerzyboksa.fishtracker.models.dto.CreateFishRequestDTO;
import com.jerzyboksa.fishtracker.repositories.FishRepository;
import com.jerzyboksa.fishtracker.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
    var user2 = TestHelper.createUser(2L, "user2@mail.com", "user2");

    var fish1 = TestHelper.createFish(1L, "Pike", user1);
    var fish2 = TestHelper.createFish(2L, "Pike", user2);
    var fish3 = TestHelper.createFish(3L, "Perch", user1);

    when(fishRepository.findAllByUserId(user1.getId())).thenReturn(List.of(fish1, fish3));
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

    assertThat(result.get(1).getId()).isEqualTo(fish3.getId());
    assertThat(result.get(1).getDate()).isNotNull();
    assertThat(result.get(1).getSpecie()).isEqualTo(fish3.getSpecie());
    assertThat(result.get(1).getSize()).isEqualTo(fish3.getSize());
    assertThat(result.get(1).getWeight()).isEqualTo(fish3.getWeight());
  }

  @Test
  void create_fish_should_create_fish() {
    //given
    var request = CreateFishRequestDTO.builder()
        .specie("specie")
        .date(LocalDateTime.now())
        .location("location")
        .build();

    var user = TestHelper.createUser(1L, "test@mail.com", "test");

    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

    //when
    sut.createFish(user.getId(), request);

    //then
    verify(fishRepository, times(1)).save(any());
  }

}