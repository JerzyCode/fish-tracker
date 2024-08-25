package com.jerzyboksa.fishtracker.models.dto;

import com.jerzyboksa.fishtracker.models.Fish;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FishLightDto {
  private Long id;
  private LocalDateTime date;
  private String specie;
  private Double size;
  private Double weight;

  public static FishLightDto of(Fish fish) {
    return FishLightDto.builder()
        .id(fish.getId())
        .date(fish.getDate())
        .specie(fish.getSpecie())
        .size(fish.getSize())
        .weight(fish.getWeight())
        .build();
  }
}
