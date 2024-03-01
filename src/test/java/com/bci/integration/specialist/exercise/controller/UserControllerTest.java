package com.bci.integration.specialist.exercise.controller;

import com.bci.integration.specialist.exercise.controller.requests.JwtRequest;
import com.bci.integration.specialist.exercise.controller.response.JwtResponse;
import com.bci.integration.specialist.exercise.dto.UserModifyDto;
import com.bci.integration.specialist.exercise.services.AuthenticationService;
import com.bci.integration.specialist.exercise.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
  @Mock
  UserService userService;
  @Mock
  AuthenticationService authenticationService;

  @InjectMocks
  UserController userController;

  @Test
  void registerUser() {
    when(userService.registerUser(any())).thenReturn(ResponseEntity.ok().build());
    assertEquals(ResponseEntity.ok().build(), this.userController.registerUser(any()));
  }

  @Test
  void createAuthenticationToken() {
    JwtResponse jwtResponse = new JwtResponse("token");
    when(authenticationService.authenticate(any(), any())).thenReturn(jwtResponse);
    assertEquals(ResponseEntity.ok(jwtResponse), this.userController.createAuthenticationToken(new JwtRequest()));
  }

  @Test
  void modifyUser() {
    when(userService.modifyUserEmailOrPassword(any(), anyString())).thenReturn(ResponseEntity.ok().build());
    assertEquals(ResponseEntity.ok().build(), this.userController.modifyUser("token", new UserModifyDto()));
  }

  @Test
  void getUserByToken() {
    when(userService.getUserByToken(any())).thenReturn(ResponseEntity.ok().build());
    assertEquals(ResponseEntity.ok().build(), this.userController.getUserByToken(any()));
  }

  @Test
  void logicDelete() {
    when(userService.logicDelete(any())).thenReturn(ResponseEntity.ok().build());
    assertEquals(ResponseEntity.ok().build(), this.userController.logicDelete(any()));
  }
}
