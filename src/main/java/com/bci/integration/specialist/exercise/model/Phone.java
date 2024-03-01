package com.bci.integration.specialist.exercise.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

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
