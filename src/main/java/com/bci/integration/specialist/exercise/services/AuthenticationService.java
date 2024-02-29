package com.bci.integration.specialist.exercise.services;

import com.bci.integration.specialist.exercise.controller.response.JwtResponse;

public interface AuthenticationService {

  JwtResponse authenticate(String username, String password);
}
