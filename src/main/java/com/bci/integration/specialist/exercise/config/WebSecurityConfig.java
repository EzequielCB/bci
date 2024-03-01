package com.bci.integration.specialist.exercise.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
  public static final String USER = "/user";
  private final UserDetailsService userDetailsService;
  private final JwtRequestFilter jwtRequestFilter;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

  public WebSecurityConfig(
      UserDetailsService userDetailsService, JwtRequestFilter jwtRequestFilter,
      JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint1) {
    this.userDetailsService = userDetailsService;
    this.jwtRequestFilter = jwtRequestFilter;
    this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint1;
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
  }

  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  protected SecurityFilterChain sharedsecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .csrf(AbstractHttpConfigurer::disable)
        .cors(AbstractHttpConfigurer::disable)
        .headers(httpSecurityHeadersConfigurer ->
            httpSecurityHeadersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
        .sessionManagement(httpSecuritySessionManagementConfigurer ->
            httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    return httpSecurity.build();
  }

  @Bean
  protected SecurityFilterChain securityFilterChainOutsideRegistration(HttpSecurity httpSecurity) throws Exception {
    sharedsecurityFilterChain(httpSecurity);
    httpSecurity
        .authorizeHttpRequests(authorize -> authorize
            .antMatchers(HttpMethod.POST, USER).permitAll()
            .antMatchers(HttpMethod.POST, "/user/authenticate").permitAll()
            .antMatchers("/h2-console/**").permitAll()
            .antMatchers("/swagger-ui/**").permitAll()
            .anyRequest().permitAll()
        );

    return httpSecurity.build();
  }

  @Bean
  protected SecurityFilterChain securityFilterChainUserRegistration(HttpSecurity httpSecurity) throws Exception {
    sharedsecurityFilterChain(httpSecurity);
    httpSecurity
        .authorizeHttpRequests(authorize -> authorize
            .antMatchers(HttpMethod.GET, USER).authenticated()
            .antMatchers(HttpMethod.PUT, USER).authenticated()
            .antMatchers(HttpMethod.DELETE, USER).authenticated()
        )
        .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class).exceptionHandling()
        .authenticationEntryPoint(jwtAuthenticationEntryPoint);

    return httpSecurity.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
      throws Exception {
    final List<GlobalAuthenticationConfigurerAdapter> configurerAdapters = new ArrayList<>();
    configurerAdapters.add(new GlobalAuthenticationConfigurerAdapter() {
      @Override public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
      }
    });
    return authenticationConfiguration.getAuthenticationManager();
  }
}
