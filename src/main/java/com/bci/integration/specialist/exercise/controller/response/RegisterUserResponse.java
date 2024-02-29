package com.bci.integration.specialist.exercise.controller.response;

import com.bci.integration.specialist.exercise.dto.UserDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class RegisterUserResponse {
  @Schema(name = "id")
  private UUID userId;

  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
  private LocalDateTime lastLogin;
  private String token;
  private boolean isActive;
  private UserDto user;
}
