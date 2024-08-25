package com.jerzyboksa.fishtracker.repositories;

import com.jerzyboksa.fishtracker.models.Fish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FishRepository extends JpaRepository<Fish, Long> {
  List<Fish> findAllByUserId(Long userId);
}
