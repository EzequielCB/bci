package com.bci.integration.specialist.exercise.dto;

import com.bci.integration.specialist.exercise.model.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

  private UUID uuid;

  private String name;

  private String email;

  private String password;

  private List<PhoneDto> phones;

  public static UserDto userConverter(Users user, List<PhoneDto> phoneDtos) {
    return UserDto.builder()
        .email(user.getEmail())
        .name(user.getName())
        .password(user.getPassword())
        .phones(phoneDtos)
        .uuid(user.getUuid())
        .build();
  }

}
