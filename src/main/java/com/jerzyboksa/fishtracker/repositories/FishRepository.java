package com.jerzyboksa.fishtracker.repositories;

import com.jerzyboksa.fishtracker.models.Fish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FishRepository extends JpaRepository<Fish, Long> {
  List<Fish> findAllByUserId(Long userId);

  @Query(value = "SELECT * FROM fish_table ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
  Optional<Fish> getRandomFish();
}
