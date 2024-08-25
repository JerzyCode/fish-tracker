package com.jerzyboksa.fishtracker.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "fish_table")
public class Fish {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NotNull
  private LocalDate date;
  @NotNull
  private String specie;
  private Double size;
  private Double weight;
  private String location;
  private String method;
  private String bait;
  private String imgPath;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;
}
