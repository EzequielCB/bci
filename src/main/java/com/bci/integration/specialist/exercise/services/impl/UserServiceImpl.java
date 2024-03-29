package com.bci.integration.specialist.exercise.services.impl;

import com.bci.integration.specialist.exercise.config.JwtTokenUtil;
import com.bci.integration.specialist.exercise.controller.requests.RegisterUserRequest;
import com.bci.integration.specialist.exercise.controller.response.GeneralBciResponse;
import com.bci.integration.specialist.exercise.controller.response.RegisterUserResponse;
import com.bci.integration.specialist.exercise.dto.PhoneDto;
import com.bci.integration.specialist.exercise.dto.UserDto;
import com.bci.integration.specialist.exercise.dto.UserModifyDto;
import com.bci.integration.specialist.exercise.model.Phone;
import com.bci.integration.specialist.exercise.model.Users;
import com.bci.integration.specialist.exercise.repository.PhoneRepository;
import com.bci.integration.specialist.exercise.repository.UsersRepository;
import com.bci.integration.specialist.exercise.services.UserService;
import com.bci.integration.specialist.exercise.utils.UUIDGenerator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Log4j2
public class UserServiceImpl implements UserService {
  private final UsersRepository userRepository;
  private final PhoneRepository phoneRepository;
  private final JwtTokenUtil jwtTokenUtil;

  @Value("${email.regex}")
  private String emailRegex;

  public UserServiceImpl(UsersRepository userRepository, PhoneRepository phoneRepository,
      JwtTokenUtil jwtTokenUtil) {
    this.userRepository = userRepository;
    this.phoneRepository = phoneRepository;
    this.jwtTokenUtil = jwtTokenUtil;
  }

  @Override public ResponseEntity<GeneralBciResponse> registerUser(RegisterUserRequest registerUserRequest) {

    log.info("Se ejecuta /user/register con el user: {}", registerUserRequest);

    //se valida el email
    if (!this.validateEmail(registerUserRequest.getEmail())) {

      log.error("El correo ingresado no coincide con la validacion hecha con el regex: {}", this.emailRegex);

      return new ResponseEntity<>(GeneralBciResponse.builder()
          .message("El correo posee un formato incorrecto")
          .build(), HttpStatus.BAD_REQUEST);
    }

    log.info("Comienza el proceso de registro.");
    //se verifica que no este registrado
    Optional<Users> userOptional;
    try {
      userOptional = this.userRepository.findByEmail(registerUserRequest.getEmail());

    } catch (Exception e) {
      return databaseError(e);
    }

    if (userOptional.isPresent()) {

      log.error("Se encontro un usuario registrado con el email: {}", registerUserRequest.getEmail());

      return new ResponseEntity<>(GeneralBciResponse.builder().
          message("El correo ya esta registrado")
          .build(), HttpStatus.CONFLICT);

    } else {

      log.info("Se creara un nuevo registro en la base. ");
      Users user = this.saveUser(registerUserRequest);

      if (Objects.nonNull(user)) {
        return ResponseEntity.ok(GeneralBciResponse.<RegisterUserResponse>builder()
            .data(RegisterUserResponse.builder()
                .token(user.getToken())
                .userId(user.getUuid())
                .createdAt(user.getCreatedAt())
                .modifiedAt(user.getUpdatedAt())
                .lastLogin(user.getLastLogin())
                .isActive(user.getIsActive())
                .user(UserDto.userConverter(user, registerUserRequest.getPhones()))
                .build())
            .build());
      } else
        return new ResponseEntity<>(GeneralBciResponse.builder()
            .message("Hubo un error en la base de datos, reintente nuevamente.")
            .build(),
            HttpStatus.INTERNAL_SERVER_ERROR);

    }
  }

  private ResponseEntity<GeneralBciResponse> databaseError(Exception e) {
    log.error("Se capturo la siguiente excepcion: {}", e);
    return new ResponseEntity<>(GeneralBciResponse.builder()
        .message("Hubo un error en la base de datos, reintente nuevamente.")
        .build(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Override public ResponseEntity<GeneralBciResponse> modifyUserEmailOrPassword(UserModifyDto dto, String token) {
    try {
      log.info("Comienza la ejecucion del ");
      if (Objects.nonNull(dto.getEmail()) && !this.validateEmail(dto.getEmail())) {

        log.error("El correo ingresado no coincide con la validacion hecha con el regex: {}", this.emailRegex);

        return new ResponseEntity<>(GeneralBciResponse.builder()
            .message("El correo posee un formato incorrecto.")
            .build(),
            HttpStatus.BAD_REQUEST);
      }
      Optional<Users> optionalUsers = this.userRepository.findByToken(token.substring(7));
      if (optionalUsers.isPresent()) {
        log.info("Se procede a modificar el user: {}", optionalUsers.get().getName());
        Users user = optionalUsers.get();
        user.setPassword(this.checkNullability(dto.getPassword()) ? user.getPassword() : dto.getPassword());
        user.setEmail(this.checkNullability(dto.getEmail()) ? user.getEmail() : dto.getEmail());
        user.setUpdatedAt(LocalDateTime.now());
        user.setToken(token.substring(7));
        user.setLastLogin(LocalDateTime.now());

        return ResponseEntity.ok(GeneralBciResponse.builder()
            .message("Usuario modificado.")
            .data(this.userRepository.save(user))
            .build());

      } else {
        return new ResponseEntity<>(GeneralBciResponse.builder()
            .message("No se encontro al usuario")
            .build(), HttpStatus.BAD_REQUEST);
      }

    } catch (Exception e) {
      return this.databaseError(e);
    }
  }

  private boolean checkNullability(Object object) {
    return Objects.isNull(object);
  }

  @Override
  public void updateUserToken(String email, String token) {
    try {
      Optional<Users> optionalUsers = this.userRepository.findByEmail(email);
      if (optionalUsers.isPresent()) {
        Users users = optionalUsers.get();
        users.setUpdatedAt(LocalDateTime.now());
        users.setToken(token);
        userRepository.save(users);
        log.info("Se logro insertar el token");
      }
    } catch (Exception e) {
      log.error("Error en la base de datos, no se logro insertar el token");
    }
  }

  @Override public ResponseEntity<GeneralBciResponse> getUserByToken(String token) {
    try {
      log.info("Se busca al usuario con el token del requestHeader");
      Optional<Users> optionalUsers = this.userRepository.findByToken(token.substring(7));
      if (optionalUsers.isPresent()) {
        return ResponseEntity.ok(GeneralBciResponse.builder()
            .data(optionalUsers.get())
            .message("Usuario encontrado.")
            .build());
      } else {
        return new ResponseEntity<>(GeneralBciResponse.builder()
            .message("El usuario no fue encontrado.")
            .build(), HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      return databaseError(e);
    }
  }

  @Override public ResponseEntity<Void> logicDelete(String token) {
    try {
      log.info("Se busca al usuario a borrar con el token del requestHeader");
      Optional<Users> optionalUsers = this.userRepository.findByToken(token.substring(7));
      if (optionalUsers.isPresent()) {
        Users users = optionalUsers.get();
        users.setLastLogin(LocalDateTime.now());
        users.setUpdatedAt(LocalDateTime.now());
        users.setIsActive(false);
        userRepository.saveAndFlush(users);
        return ResponseEntity.ok().build();
      } else {
        return ResponseEntity.notFound().build();
      }
    } catch (Exception e) {
      return ResponseEntity.internalServerError().build();
    }
  }

  private Users saveUser(RegisterUserRequest registerUserRequest) {
    try {
      Users user = Users.builder()
          .uuid(UUIDGenerator.generateType5UUID(registerUserRequest.getEmail()))
          .token(jwtTokenUtil.generateNewUserToken(registerUserRequest.getName()))
          .createdAt(LocalDateTime.now())
          .name(registerUserRequest.getName())
          .password(registerUserRequest.getPassword())
          .email(registerUserRequest.getEmail())
          .lastLogin(LocalDateTime.now())
          .isActive(Boolean.TRUE)
          .build();
      user.setPhones(this.getPhones(registerUserRequest.getPhones(), this.userRepository.save(user)));
      return this.userRepository.saveAndFlush(user);
    } catch (Exception e) {
      log.error("No se pudo guardar el usuario por la siguiente excepcion: {}", e);
      return null;
    }
  }

  private List<Phone> getPhones(List<PhoneDto> phonesDto, Users name) {
    List<Phone> phones = new ArrayList<>();
    phonesDto.forEach(phoneDto -> {
      if (Objects.nonNull(phoneDto.getNumber()) &&
          Objects.nonNull(phoneDto.getCityCode()) &&
          Objects.nonNull(phoneDto.getCountryCode())) {
        try {
          phones.add(this.phoneRepository.save(
              Phone.builder()
                  .cityCode(phoneDto.getCityCode())
                  .countryCode(phoneDto.getCountryCode())
                  .number(phoneDto.getNumber())
                  .user(name)
                  .build()));
        } catch (Exception e) {
          log.error("No se pudo guardar el telefono por la siguiente excepcion: {}", e);
        }
      }
    });
    return phones;
  }

  private boolean validateEmail(String email) {
    return Pattern.compile(emailRegex).matcher(email).matches();
  }

}
