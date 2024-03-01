package com.bci.integration.specialist.exercise.services;

import com.bci.integration.specialist.exercise.config.JwtTokenUtil;
import com.bci.integration.specialist.exercise.controller.response.JwtResponse;
import com.bci.integration.specialist.exercise.services.impl.AuthenticationServiceImpl;
import com.bci.integration.specialist.exercise.services.impl.JwtUserDetailsServiceImpl;
import com.bci.integration.specialist.exercise.services.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
  @Mock
  AuthenticationManager authenticationManager;
  @Mock
  JwtTokenUtil jwtTokenUtil;
  @Mock
  JwtUserDetailsServiceImpl userDetailsService;
  @Mock
  UserService userService;
  @InjectMocks
  AuthenticationServiceImpl authenticationService;

  @Test
  void whenAuthenticateThenOk() {
    JwtResponse jwtResponse = new JwtResponse(TestUtils.TOKEN);
    when(this.userDetailsService.loadUserByUsername(any())).thenReturn(TestUtils.buildUserDetails());
    when(this.jwtTokenUtil.generateToken(any())).thenReturn(TestUtils.TOKEN);
    when(this.authenticationManager.authenticate(any())).thenReturn(TestUtils.buildAuthentication());
    doNothing().when(userService).updateUserToken(anyString(), anyString());
    assertEquals(TestUtils.TOKEN, this.authenticationService.authenticate("user", TestUtils.TOKEN).getJwtToken());
  }

  @Test
  void whenAuthenticateThenDisabledExc() {
    JwtResponse jwtResponse = new JwtResponse(TestUtils.TOKEN);
    when(this.userDetailsService.loadUserByUsername(any())).thenThrow(DisabledException.class);
    assertThrows(DisabledException.class,
        () -> this.authenticationService.authenticate("user", TestUtils.TOKEN).getJwtToken());
  }

  @Test
  void whenAuthenticateThenBadCredencials() {
    JwtResponse jwtResponse = new JwtResponse(TestUtils.TOKEN);
    when(this.userDetailsService.loadUserByUsername(any())).thenThrow(BadCredentialsException.class);
    assertThrows(BadCredentialsException.class,
        () -> this.authenticationService.authenticate("user", TestUtils.TOKEN).getJwtToken());
  }
}
