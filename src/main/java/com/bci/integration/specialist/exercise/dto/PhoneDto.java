package com.bci.integration.specialist.exercise.dto;

import com.bci.integration.specialist.exercise.model.Phone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhoneDto {

  private String number;

  private String cityCode;

  private String countryCode;

  public static List<PhoneDto> phoneListConverter(List<Phone> phones) {
    List<PhoneDto> phoneDtos = new ArrayList<>();
    phones.forEach(phone -> {
      if (Objects.nonNull(phone.getNumber()) &&
          Objects.nonNull(phone.getCityCode()) &&
          Objects.nonNull(phone.getCountryCode())) {
        phoneDtos.add(
            PhoneDto.builder()
                .cityCode(phone.getCityCode())
                .countryCode(phone.getCountryCode())
                .number(phone.getNumber())
                .build());
      }
    });
    return phoneDtos;
  }
}

