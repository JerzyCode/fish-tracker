package com.jerzyboksa.fishtracker.models.dto;

import com.jerzyboksa.fishtracker.models.Fish;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FishLightDTO {
  private Long id;
  private String date;
  private String specie;
  private Double size;
  private Double weight;

  public static FishLightDTO of(Fish fish) {
    return FishLightDTO.builder()
        .id(fish.getId())
        .date(String.valueOf(fish.getDate()))
        .specie(fish.getSpecie())
        .size(fish.getSize())
        .weight(fish.getWeight())
        .build();
  }

}
