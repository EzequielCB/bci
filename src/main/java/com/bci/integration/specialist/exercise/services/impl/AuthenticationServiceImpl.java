package com.bci.integration.specialist.exercise.services.impl;

import com.bci.integration.specialist.exercise.config.JwtTokenUtil;
import com.bci.integration.specialist.exercise.controller.response.JwtResponse;
import com.bci.integration.specialist.exercise.services.AuthenticationService;
import com.bci.integration.specialist.exercise.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Log4j2
public class AuthenticationServiceImpl implements AuthenticationService {
  private final AuthenticationManager authenticationManager;
  private final JwtTokenUtil jwtTokenUtil;
  private final JwtUserDetailsServiceImpl userDetailsService;
  private final UserService userService;

  @Autowired
  public AuthenticationServiceImpl(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil,
      JwtUserDetailsServiceImpl userDetailsService, UserService userService) {
    this.authenticationManager = authenticationManager;
    this.jwtTokenUtil = jwtTokenUtil;
    this.userDetailsService = userDetailsService;
    this.userService = userService;
  }

  @Override public JwtResponse authenticate(String username, String password) {
    try {
      final UserDetails userDetails = userDetailsService
          .loadUserByUsername(username);

      final String token = jwtTokenUtil.generateToken(userDetails);

      log.info("Se intenta loguear el user: {}", username);
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>()));

      this.userService.updateUserToken(username, token);

      return new JwtResponse(token);

    } catch (DisabledException e) {
      throw new DisabledException("USER_DISABLED", e);
    } catch (BadCredentialsException e) {
      throw new BadCredentialsException("INVALID_CREDENTIALS", e);
    }
  }

}
