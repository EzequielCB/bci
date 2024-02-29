package com.bci.integration.specialist.exercise.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class Users {

  @Column(name = "ID")
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_generator")
  @SequenceGenerator(name = "users_generator", sequenceName = "users_seq",
      allocationSize = 1)
  private Integer id;

  @Column(name = "UUID", unique = true, columnDefinition = "RAW(16)")
  @NotNull
  private UUID uuid;

  private String token;

  @Column(name = "NAME", unique = true)
  @NotNull
  private String name;

  @Column(name = "EMAIL", unique = true)
  @NotNull
  private String email;

  @Column(name = "PASSWORD", unique = true)
  @NotNull
  private String password;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Phone> phones;

  @Column(name = "CREATED_AT")
  private LocalDateTime createdAt;

  @Column(name = "UPDATED_AT")
  private LocalDateTime updatedAt;

  @Column(name = "LAST_LOGIN")
  private LocalDateTime lastLogin;

  @Column(name = "IS_ACTIVE")
  private Boolean isActive;

}
