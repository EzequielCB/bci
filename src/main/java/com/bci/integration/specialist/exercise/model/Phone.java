package com.bci.integration.specialist.exercise.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@Entity
@Table(name = "phone")
@NoArgsConstructor
@AllArgsConstructor
public class Phone {

  @Column(name = "ID")
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "phone_generator")
  @SequenceGenerator(name = "phone_generator", sequenceName = "phone_seq",
      allocationSize = 1)
  private Integer id;

  @Column(name = "NUMBER", unique = true)
  @NotNull
  private String number;

  @Column(name = "CITY_CODE", unique = true)
  @NotNull
  private String cityCode;

  @Column(name = "COUNTRY_CODE", unique = true)
  @NotNull
  private String countryCode;

  @ManyToOne
  @JoinColumn(name = "USER_ID")
  @JsonIgnore
  private Users user;
}
