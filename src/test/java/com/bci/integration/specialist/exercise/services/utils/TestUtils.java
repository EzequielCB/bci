package com.bci.integration.specialist.exercise.services.utils;

import com.bci.integration.specialist.exercise.controller.requests.RegisterUserRequest;
import com.bci.integration.specialist.exercise.dto.PhoneDto;
import com.bci.integration.specialist.exercise.dto.UserModifyDto;
import com.bci.integration.specialist.exercise.model.Users;
import com.bci.integration.specialist.exercise.utils.UUIDGenerator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TestUtils {
  public static final String MAIL_OK = "email@domain.cl";
  public static final String MAIL_NOT_OK = "ee";
  public static final String TOKEN = "token";

  public static Users buildUserWithRegUserReq(RegisterUserRequest registerUserRequest) {
    return Users.builder()
        .uuid(UUIDGenerator.generateType5UUID(registerUserRequest.getName()))
        .token("token")
        .createdAt(LocalDateTime.now())
        .name(registerUserRequest.getName())
        .password(registerUserRequest.getPassword())
        .email(registerUserRequest.getEmail())
        .lastLogin(LocalDateTime.now())
        .isActive(Boolean.TRUE)
        .build();
  }

  public static RegisterUserRequest buildRegisterUserRequest(String email) {
    List<PhoneDto> phoneList = new ArrayList<>();
    phoneList.add(PhoneDto.builder()
        .cityCode("11")
        .countryCode("54")
        .number("11223344")
        .build());
    return RegisterUserRequest.builder()
        .email(email)
        .name("Ric")
        .password("Ric")
        .phones(phoneList)
        .build();
  }

  public static UserModifyDto buildRegisterUserModifyRequest(String email) {
    return UserModifyDto.builder()
        .email(email)
        .password("pass")
        .build();
  }

  public static UserDetails buildUserDetails() {
    return new UserDetails() {
      @Override public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
      }

      @Override public String getPassword() {
        return null;
      }

      @Override public String getUsername() {
        return null;
      }

      @Override public boolean isAccountNonExpired() {
        return false;
      }

      @Override public boolean isAccountNonLocked() {
        return false;
      }

      @Override public boolean isCredentialsNonExpired() {
        return false;
      }

      @Override public boolean isEnabled() {
        return false;
      }
    };
  }

  public static Authentication buildAuthentication() {
    return new Authentication() {
      @Override public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
      }

      @Override public Object getCredentials() {
        return null;
      }

      @Override public Object getDetails() {
        return null;
      }

      @Override public Object getPrincipal() {
        return null;
      }

      @Override public boolean isAuthenticated() {
        return false;
      }

      @Override public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

      }

      @Override public String getName() {
        return null;
      }
    };
  }
}
