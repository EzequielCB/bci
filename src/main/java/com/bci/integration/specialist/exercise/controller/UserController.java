package com.bci.integration.specialist.exercise.controller;

import com.bci.integration.specialist.exercise.controller.requests.JwtRequest;
import com.bci.integration.specialist.exercise.controller.requests.RegisterUserRequest;
import com.bci.integration.specialist.exercise.controller.response.GeneralBciResponse;
import com.bci.integration.specialist.exercise.controller.response.JwtResponse;
import com.bci.integration.specialist.exercise.dto.UserModifyDto;
import com.bci.integration.specialist.exercise.services.AuthenticationService;
import com.bci.integration.specialist.exercise.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
  private final UserService userService;

  private final AuthenticationService authenticationService;

  public UserController(UserService userService, AuthenticationService authenticationService) {
    this.userService = userService;
    this.authenticationService = authenticationService;

  }

  @Operation(summary = "Registra un usuario en el sistema", tags = {
      "user" })
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = GeneralBciResponse.class))),

      @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = GeneralBciResponse.class))),

      @ApiResponse(responseCode = "500", description = "Unexpected Error", content = @Content(schema = @Schema(implementation = GeneralBciResponse.class))) })
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<GeneralBciResponse> registerUser(@RequestBody RegisterUserRequest registerUserRequest) {
    return this.userService.registerUser(registerUserRequest);
  }

  @Operation(summary = "Loguea a un usuario en el sistema", tags = {
      "user" })
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),

      @ApiResponse(responseCode = "403", description = "Bad request", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),

      @ApiResponse(responseCode = "500", description = "Unexpected Error", content = @Content(schema = @Schema(implementation = ResponseEntity.class))) })
  @PostMapping(value = "/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<JwtResponse> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {
    return ResponseEntity.ok(
        authenticationService.authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
  }

  @Operation(summary = "Modifica un usuario", tags = {
      "user" })
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),

      @ApiResponse(responseCode = "403", description = "Bad request", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),

      @ApiResponse(responseCode = "500", description = "Unexpected Error", content = @Content(schema = @Schema(implementation = ResponseEntity.class))) })
  @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<GeneralBciResponse> modifyUser(@RequestHeader("Authorization") String token,
      @RequestBody UserModifyDto dto) {
    return this.userService.modifyUserEmailOrPassword(dto, token);
  }

  @Operation(summary = "Obtiene un usuario por name", tags = {
      "user" })
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),

      @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),

      @ApiResponse(responseCode = "500", description = "Database error", content = @Content(schema = @Schema(implementation = ResponseEntity.class))) })
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<GeneralBciResponse> getUserByToken(@RequestHeader("Authorization") String token) {
    return this.userService.getUserByToken(token);
  }

  @Operation(summary = "Obtiene un usuario por token", tags = {
      "user" })
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),

      @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),

      @ApiResponse(responseCode = "500", description = "Database error", content = @Content(schema = @Schema(implementation = ResponseEntity.class))) })
  @DeleteMapping()
  public ResponseEntity<Void> logicDelete(@RequestHeader("Authorization") String token) {
    return this.userService.logicDelete(token);
  }

}
