package com.bci.integration.specialist.exercise.services.impl;

import com.bci.integration.specialist.exercise.model.Users;
import com.bci.integration.specialist.exercise.repository.UsersRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

  private final UsersRepository userRepository;

  public JwtUserDetailsServiceImpl(UsersRepository userRepository) {
    this.userRepository = userRepository;
  }

  private PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2A);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<Users> optionalUsers = this.userRepository.findByName(username);
    if (optionalUsers.isPresent()) {

      Users users = optionalUsers.get();
      users.setLastLogin(LocalDateTime.now());
      userRepository.saveAndFlush(users);

      return new User(username, passwordEncoder().encode(users.getPassword()),
          new ArrayList<>());
    } else {
      throw new UsernameNotFoundException("User not found with username: " + username);
    }
  }

}
