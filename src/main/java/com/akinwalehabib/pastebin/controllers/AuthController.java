package com.akinwalehabib.pastebin.controllers;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.akinwalehabib.pastebin.controllers.exceptions.BadRequestException;
import com.akinwalehabib.pastebin.controllers.response.LoginResponse;
import com.akinwalehabib.pastebin.data.User;
import com.akinwalehabib.pastebin.security.LoginRequest;
import com.akinwalehabib.pastebin.security.SecurityUser;
import com.akinwalehabib.pastebin.services.AuthService;
import com.akinwalehabib.pastebin.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping(
  path = "/api/v1/auth",
  produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

  private AuthService authService;
  private UserService userService;
  private final AuthenticationManager authenticationManager;
  ObjectMapper objectMapper;

  public AuthController(UserService userService, AuthService authService, AuthenticationManager authenticationManager, ObjectMapper objectMapper, PasswordEncoder passwordEncoder) {
    this.userService = userService;
    this.authService = authService;
    this.authenticationManager = authenticationManager;
    this.objectMapper = objectMapper;
  }

  @PostMapping(
    path = "/signup",
    consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.CREATED)
  public void createUser(@Valid @RequestBody User userObj, Errors errors, HttpServletRequest request) {
    if (errors.hasErrors()) {
      List<String> errorList = new ArrayList<>();
      List<ObjectError> objectErrorList = errors.getAllErrors();
      for (ObjectError objectError : objectErrorList) {
        errorList.add(MessageFormat.format("{0}", objectError.getDefaultMessage()));
      }

      throw new BadRequestException("Invalid user details.", errorList);
    }

    try {
      userService.addUser(userObj);
    } catch (DataIntegrityViolationException e) {
      throw new BadRequestException("Email taken.");
    } catch (IllegalArgumentException e) {
      List<String> ee = new ArrayList<>();
      throw new BadRequestException("Error occured while saving details to DB", ee);
    }
  }

  @PostMapping(
    path = "/login",
    consumes = MediaType.APPLICATION_JSON_VALUE)
  public LoginResponse signin(@RequestBody LoginRequest loginRequest) {
    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));

    SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
    return new LoginResponse(securityUser.getUser(), authService.generateToken(authentication));
  }

  @PostMapping("/token")
  public String token(Authentication authentication) {
    return authService.generateToken(authentication);
  }

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public Page<User> getUsers() {
    return userService.getUsersByPage(0, 10);
  }
}
