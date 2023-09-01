package com.akinwalehabib.pastebin.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.akinwalehabib.pastebin.data.User;
import com.akinwalehabib.pastebin.data.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@TestPropertySource(
  locations = "classpath:application-it.properties"
)
@AutoConfigureMockMvc
public class AuthIT {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    User user = new User("user@test.com", "password", "firstName", "lastName", "READ, ROLE_USER");
    userRepository.save(user);
    userRepository.flush();
  }

  @AfterEach
  void deleteAll() {
    userRepository.deleteAll();
  }
  
  @Test
  void createUser() throws JsonProcessingException, Exception {
    userRepository.deleteAll();
    assertThat(userRepository.findAll().size()).isEqualTo(0);
    User user = new User("user@test.com", "password", "firstName", "lastName", "READ, ROLE_USER");
    
    ResultActions resultActions = mockMvc.perform(post("/api/v1/auth/signup")
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .content(objectMapper.writeValueAsString(user)));

    resultActions.andExpect(MockMvcResultMatchers.status().isCreated());

    List<User> users = userRepository.findAll();
    assertThat(users.size()).isEqualTo(1);
    
    assertThat(users)
    .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "apiKey", "password")
            .contains(user);
  }

  @Test
  void createUserReturns401WhenIncomplete() throws JsonProcessingException, Exception {
    userRepository.deleteAll();
    assertThat(userRepository.findAll().size()).isEqualTo(0);
    User user = new User();
    user.setPassword("password");
    user.setFirstName("firstName");
    user.setLastName("lastName");
    
    ResultActions resultActions = mockMvc.perform(post("/api/v1/auth/signup")
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .content(objectMapper.writeValueAsString(user)));

    resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());

    List<User> users = userRepository.findAll();
    assertThat(users.size()).isEqualTo(0);
  }

  @Test
  void createUserReturns401WhenEmailExists() throws JsonProcessingException, Exception {
    assertThat(userRepository.findByEmail("user@test.com")).isNotNull();
    
    User user = new User("user@test.com", "password", "firstName", "lastName", "READ, ROLE_USER");
    ResultActions resultActions = mockMvc.perform(post("/api/v1/auth/signup")
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .content(objectMapper.writeValueAsString(user)));
    
    resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
    
    MvcResult mvcResult = resultActions.andReturn();
    assertThat(mvcResult.getResponse().getContentAsString()).contains("Email taken");
  }

  @Test
  void login() throws JsonProcessingException, Exception {
    assertThat(userRepository.findByEmail("user@test.com")).isNotNull();
    
    Map<String, String> loginRequest = new HashMap<>();
    loginRequest.put("email", "user@test.com");
    loginRequest.put("password", "password");
    ResultActions resultActions = mockMvc.perform(post("/api/v1/auth/login")
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .content(objectMapper.writeValueAsString(loginRequest)));
    
    resultActions.andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  void loginReturns401WhenIncomplete() throws JsonProcessingException, Exception {
    Map<String, String> loginRequest = new HashMap<>();
    loginRequest.put("password", "password");
    ResultActions resultActions = mockMvc.perform(post("/api/v1/auth/login")
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .content(objectMapper.writeValueAsString(loginRequest)));
    
    resultActions.andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  @Test
  void loginReturns401WhenIncorrect() throws JsonProcessingException, Exception {
    assertThat(userRepository.findByEmail("user@test.com")).isNotNull();
    String password =  userRepository.findByEmail("user@test.com").get().getPassword();
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    assertThat(passwordEncoder.matches("password", password)).isTrue();
    
    Map<String, String> loginRequest = new HashMap<>();
    loginRequest.put("email", "user@test.com");
    ResultActions resultActions = mockMvc.perform(post("/api/v1/auth/login")
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .content(objectMapper.writeValueAsString(loginRequest)));
    
    resultActions.andExpect(MockMvcResultMatchers.status().isUnauthorized());

    loginRequest.put("password", "password");
    loginRequest.remove("email");
    resultActions = mockMvc.perform(post("/api/v1/auth/login")
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .content(objectMapper.writeValueAsString(loginRequest)));
    
    resultActions.andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }
}
