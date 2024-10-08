package com.jerzyboksa.fishtracker;

import com.jerzyboksa.fishtracker.models.Fish;
import com.jerzyboksa.fishtracker.models.User;

import java.time.LocalDate;
import java.util.Random;

public class TestHelper {
  public static Fish createFish(Long id, String specie, User user) {
    Random random = new Random();

    return Fish.builder()
        .id(id)
        .date(LocalDate.now().minusDays(12))
        .specie(specie)
        .size(random.nextDouble() * 50)
        .weight(random.nextDouble() * 50)
        .location("location")
        .method("method")
        .bait("bait")
        .imageName("imageName")
        .user(user)
        .build();
  }

  public static User createUser(Long id, String email, String name) {
    return User.builder()
        .id(id)
        .email(email)
        .name(name)
        .password("password123@#")
        .build();
  }
}
