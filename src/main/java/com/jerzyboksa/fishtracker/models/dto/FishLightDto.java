package com.jerzyboksa.fishtracker.models.dto;

import com.jerzyboksa.fishtracker.models.Fish;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@Builder
public class FishLightDto {
  private Long id;
  private String date;
  private String specie;
  private Double size;
  private Double weight;

  public static FishLightDto of(Fish fish) {
    return FishLightDto.builder()
        .id(fish.getId())
        .date(formatDate(fish.getDate()))
        .specie(fish.getSpecie())
        .size(fish.getSize())
        .weight(fish.getWeight())
        .build();
  }

  private static String formatDate(LocalDate date) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    return date.format(formatter);

  }
}
