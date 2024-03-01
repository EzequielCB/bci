package com.bci.integration.specialist.exercise.services;

import com.bci.integration.specialist.exercise.config.JwtTokenUtil;
import com.bci.integration.specialist.exercise.controller.requests.RegisterUserRequest;
import com.bci.integration.specialist.exercise.model.Users;
import com.bci.integration.specialist.exercise.repository.PhoneRepository;
import com.bci.integration.specialist.exercise.repository.UsersRepository;
import com.bci.integration.specialist.exercise.services.impl.UserServiceImpl;
import com.bci.integration.specialist.exercise.services.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
  @Mock
  UsersRepository userRepository;
  @Mock
  PhoneRepository phoneRepository;
  @Mock
  JwtTokenUtil jwtTokenUtil;
  @InjectMocks
  UserServiceImpl userService;

  void setUpEmailRegex() {
    ReflectionTestUtils.setField(userService, "emailRegex", "^(.+)@(.+)$");
  }

  @Test
  void whenRegisterUserThenOk() {
    setUpEmailRegex();

    RegisterUserRequest registerUserRequest = TestUtils.buildRegisterUserRequest(TestUtils.MAIL_OK);
    Users users = TestUtils.buildUserWithRegUserReq(registerUserRequest);
    when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
    when(jwtTokenUtil.generateNewUserToken(any())).thenReturn("token");
    when(userRepository.save(any())).thenReturn(users);
    when(userRepository.saveAndFlush(any())).thenReturn(users);

    assertEquals(ResponseEntity.ok().build().getStatusCode(),
        this.userService.registerUser(registerUserRequest).getStatusCode());
  }

  @Test
  void whenRegisterUserThen400() {
    setUpEmailRegex();
    RegisterUserRequest registerUserRequest = TestUtils.buildRegisterUserRequest(TestUtils.MAIL_NOT_OK);
    assertEquals(ResponseEntity.badRequest().build().getStatusCode(),
        this.userService.registerUser(registerUserRequest).getStatusCode());
  }

  @Test
  void whenRegisterUserThen409() {
    setUpEmailRegex();
    RegisterUserRequest registerUserRequest = TestUtils.buildRegisterUserRequest(TestUtils.MAIL_OK);
    when(userRepository.findByEmail(any())).thenReturn(
        Optional.of(TestUtils.buildUserWithRegUserReq(registerUserRequest)));
    assertEquals(new ResponseEntity<>(HttpStatus.CONFLICT).getStatusCode(),
        this.userService.registerUser(registerUserRequest).getStatusCode());
  }

  @Test
  void whenRegisterUserThen500AtSave() {
    setUpEmailRegex();

    when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
    when(jwtTokenUtil.generateNewUserToken(any())).thenReturn("token");
    when(userRepository.save(any())).thenThrow(RuntimeException.class);

    assertEquals(ResponseEntity.internalServerError().build().getStatusCode(),
        this.userService.registerUser(TestUtils.buildRegisterUserRequest(TestUtils.MAIL_OK)).getStatusCode());
  }

  @Test
  void whenRegisterUserThen500AtFind() {
    setUpEmailRegex();

    RegisterUserRequest registerUserRequest = TestUtils.buildRegisterUserRequest(TestUtils.MAIL_OK);
    when(userRepository.findByEmail(any())).thenThrow(RuntimeException.class);

    assertEquals(ResponseEntity.internalServerError().build().getStatusCode(),
        this.userService.registerUser(registerUserRequest).getStatusCode());
  }

  @Test
  void whenModifyUserEmailOrPasswordThenOk() {
    setUpEmailRegex();
    Users users = TestUtils.buildUserWithRegUserReq(TestUtils.buildRegisterUserRequest(TestUtils.MAIL_OK));

    when(userRepository.findByToken(any())).thenReturn(Optional.of(users));

    when(userRepository.save(any())).thenReturn(users);
    assertEquals(ResponseEntity.ok().build().getStatusCode(),
        this.userService.modifyUserEmailOrPassword(TestUtils.buildRegisterUserModifyRequest(TestUtils.MAIL_OK),
            "token:token").getStatusCode());
  }

  @Test
  void whenModifyUserEmailOrPasswordThen400() {
    setUpEmailRegex();

    assertEquals(ResponseEntity.badRequest().build().getStatusCode(),
        this.userService.modifyUserEmailOrPassword(TestUtils.buildRegisterUserModifyRequest(TestUtils.MAIL_NOT_OK),
            "token:token").getStatusCode());
  }

  @Test
  void whenModifyUserEmailOrPasswordThen500() {
    setUpEmailRegex();

    when(userRepository.findByToken(any())).thenReturn(
        Optional.of(TestUtils.buildUserWithRegUserReq(TestUtils.buildRegisterUserRequest(TestUtils.MAIL_OK))));
    when(userRepository.save(any())).thenThrow(RuntimeException.class);
    assertEquals(ResponseEntity.internalServerError().build().getStatusCode(),
        this.userService.modifyUserEmailOrPassword(TestUtils.buildRegisterUserModifyRequest(TestUtils.MAIL_OK),
            "token:token").getStatusCode());
  }

  @Test
  void updateUserToken() {
    when(userRepository.findByEmail(any())).thenReturn(
        Optional.of(TestUtils.buildUserWithRegUserReq(TestUtils.buildRegisterUserRequest(TestUtils.MAIL_OK))));
    this.userService.updateUserToken(TestUtils.MAIL_OK, "token:token");
    verify(userRepository, times(1)).findByEmail(anyString());
  }

  @Test
  void whenGetUserByTokenThen200() {
    when(userRepository.findByToken(any())).thenReturn(
        Optional.of(TestUtils.buildUserWithRegUserReq(TestUtils.buildRegisterUserRequest(TestUtils.MAIL_OK))));
    assertEquals(ResponseEntity.ok().build().getStatusCode(),
        this.userService.getUserByToken("token:token").getStatusCode());
  }

  @Test
  void whenGetUserByTokenThen404() {
    when(userRepository.findByToken(any())).thenReturn(Optional.empty());
    assertEquals(ResponseEntity.notFound().build().getStatusCode(),
        this.userService.getUserByToken("token:token").getStatusCode());
  }

  @Test
  void whenGetUserByTokenThen500() {
    when(userRepository.findByToken(any())).thenThrow(RuntimeException.class);
    assertEquals(ResponseEntity.internalServerError().build().getStatusCode(),
        this.userService.getUserByToken("token:token").getStatusCode());
  }

  @Test
  void whenLogicDeleteThenOk() {
    when(userRepository.findByToken(any())).thenReturn(
        Optional.of(TestUtils.buildUserWithRegUserReq(TestUtils.buildRegisterUserRequest(TestUtils.MAIL_OK))));
    assertEquals(ResponseEntity.ok().build().getStatusCode(),
        this.userService.logicDelete("token:token").getStatusCode());
  }

  @Test
  void whenLogicDeleteThen404() {
    when(userRepository.findByToken(any())).thenReturn(Optional.empty());
    assertEquals(ResponseEntity.notFound().build().getStatusCode(),
        this.userService.logicDelete("token:token").getStatusCode());
  }

  @Test
  void whenLogicDeleteThen500() {
    when(userRepository.findByToken(any())).thenThrow(RuntimeException.class);
    assertEquals(ResponseEntity.internalServerError().build().getStatusCode(),
        this.userService.logicDelete("token:token").getStatusCode());
  }
}
