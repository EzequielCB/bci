package com.bci.integration.specialist.exercise.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class GeneralBciResponse<T> {

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String message;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private T data;

}
