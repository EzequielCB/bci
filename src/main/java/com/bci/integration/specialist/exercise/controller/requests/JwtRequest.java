package com.bci.integration.specialist.exercise.controller.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JwtRequest {

  private String username;
  private String password;
}
