package com.bci.integration.specialist.exercise.services;

import com.bci.integration.specialist.exercise.controller.requests.RegisterUserRequest;
import com.bci.integration.specialist.exercise.controller.response.GeneralBciResponse;
import com.bci.integration.specialist.exercise.dto.UserModifyDto;
import org.springframework.http.ResponseEntity;

public interface UserService {
  GeneralBciResponse registerUser(RegisterUserRequest registerUserRequest);

  GeneralBciResponse modifyUserEmailOrPassword(UserModifyDto dto, String token);

  void updateUserToken(String name, String token);

  GeneralBciResponse getUserByToken(String token);

  ResponseEntity<Void> logicDelete(String token);

}
