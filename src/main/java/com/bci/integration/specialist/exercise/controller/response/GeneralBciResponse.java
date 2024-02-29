package com.bci.integration.specialist.exercise.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class GeneralBciResponse<T> {

  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Schema(description = "Código de respuesta Http")
  private Integer code;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Schema(description = "Descripción")
  private String message;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private T data;

  public GeneralBciResponse(Integer responseCode) {
    this.code = responseCode;
  }

  public GeneralBciResponse(Integer code, String message, T body) {
    this.setData(body);
    this.setCode(code);
    this.setMessage(message);
  }

  public GeneralBciResponse(Integer code, String message) {
    this.setCode(code);
    this.setMessage(message);
  }

  public GeneralBciResponse(T body) {
    this.setData(body);
  }

}
