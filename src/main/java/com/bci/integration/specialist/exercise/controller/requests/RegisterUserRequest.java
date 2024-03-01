package com.bci.integration.specialist.exercise.controller.requests;

import com.bci.integration.specialist.exercise.dto.PhoneDto;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Builder
public class RegisterUserRequest {
  private String name;
  private String email;

  @ToString.Exclude
  private String password;

  private List<PhoneDto> phones;
}

