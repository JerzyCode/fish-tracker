package com.jerzyboksa.fishtracker.models.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CreateFishRequestDTO {
  private LocalDateTime date;
  private String specie;
  private Double size;
  private Double weight;
  private String location;
  private String method;
  private String bait;
  private String imgPath; //TODO tutaj image ma przyjść jako cos innego nie string
}
