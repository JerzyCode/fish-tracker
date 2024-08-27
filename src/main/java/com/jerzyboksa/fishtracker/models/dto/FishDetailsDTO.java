package com.jerzyboksa.fishtracker.models.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class FishDetailsDTO {
  private Long id;
  private LocalDate date;
  private String specie;
  private Double size;
  private Double weight;
  private String location;
  private String method;
  private String bait;
  private Long userId;
  private String username;
}
