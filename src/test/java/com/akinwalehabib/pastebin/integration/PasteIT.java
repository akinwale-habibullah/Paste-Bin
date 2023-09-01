package com.akinwalehabib.pastebin.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.akinwalehabib.pastebin.controllers.response.LoginResponse;
import com.akinwalehabib.pastebin.controllers.response.PasteResponse;
import com.akinwalehabib.pastebin.controllers.response.PasteResponseListResponse;
import com.akinwalehabib.pastebin.data.Paste;
import com.akinwalehabib.pastebin.data.PasteRepository;
import com.akinwalehabib.pastebin.data.User;
import com.akinwalehabib.pastebin.data.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SpringBootTest
@TestPropertySource(
  locations = "classpath:application-it.properties"
)
@AutoConfigureMockMvc
public class PasteIT {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private PasteRepository pasteRepository;
  @Autowired
  private UserRepository userRepository;

  String jwt;
  User user;

  @BeforeEach
  void setUp() throws JsonProcessingException, Exception {
    User user = new User("user@test.com", "password", "firstName", "lastName", "READ, ROLE_USER");
    userRepository.save(user);
    userRepository.flush();
    this.user = user;

    Map<String, String> loginRequest = new HashMap<>();
    loginRequest.put("email", "user@test.com");
    loginRequest.put("password", "password");
    MvcResult mvcResult = mockMvc.perform(post("/api/v1/auth/login")
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .content(objectMapper.writeValueAsString(loginRequest)))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andReturn();
    String response = mvcResult.getResponse().getContentAsString();
    LoginResponse authResponse = objectMapper.readValue(response, LoginResponse.class);
    this.jwt = authResponse.getToken();
  }

  @AfterEach
  void deleteAll() {
    pasteRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  void createPaste() throws JsonProcessingException, Exception {
    assertThat(pasteRepository.findAll().size()).isEqualTo(0);

    Paste paste = new Paste();
    paste.setContent("demoContent");
    paste.setName("demoName");
    
    ResultActions resultActions = mockMvc
        .perform(post("/api/v1/pastes")
        .header("Authorization", "Bearer " + this.jwt)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(paste)));

    resultActions.andExpect(MockMvcResultMatchers.status().isOk());

    List<Paste> pastes = pasteRepository.findAll();
    assertThat(pastes.size()).isEqualTo(1);
    assertThat(pastes)
    .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "apiKey", "user")
            .contains(paste);
  }

  @Test
  void createPasteReturns400IfValidationFails() throws JsonProcessingException, Exception {
    Paste paste = new Paste();
    paste.setName("name");
    
    ResultActions resultActions = mockMvc
        .perform(post("/api/v1/pastes")
        .header("Authorization", "Bearer " + this.jwt)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(paste)));

    resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());

    List<Paste> pastes = pasteRepository.findAll();
    assertThat(pastes.size()).isEqualTo(0);
  }

  @Test
  void createPasteIsProtected() throws JsonProcessingException, Exception {
    assertThat(pasteRepository.findAll().size()).isEqualTo(0);

    Paste paste = new Paste();
    paste.setContent("demoContent");
    paste.setName("demoName");
    
    ResultActions resultActions = mockMvc
        .perform(post("/api/v1/pastes")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(paste)));

    resultActions.andExpect(MockMvcResultMatchers.status().isUnauthorized());

    List<Paste> pastes = pasteRepository.findAll();
    assertThat(pastes.size()).isEqualTo(0);
  }

  @Test
  void getAllPastes() throws Exception {    
    Paste paste = new Paste();
    paste.setContent("demoContent");
    paste.setName("demoName");
    pasteRepository.save(paste);
    pasteRepository.flush();
    assertThat(pasteRepository.findAll().size()).isEqualTo(1);
    
    ResultActions resultActions = mockMvc
        .perform(get("/api/v1/pastes")
        .header("Authorization", "Bearer " + this.jwt)
        .contentType(MediaType.APPLICATION_JSON));
    resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    
    MvcResult mvcResult = resultActions.andReturn();
    PasteResponseListResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), PasteResponseListResponse.class);
    assertThat(response.getData().size()).isEqualTo(1);
  }

  @Test
  void getAllPastesByPage() throws Exception {
    pasteRepository.deleteAll();
    addPastes();
    assertThat(pasteRepository.findAll().size()).isEqualTo(4);

    ResultActions resultActions = mockMvc
        .perform(get("/api/v1/pastes?page=0&size=3")
        .header("Authorization", "Bearer " + this.jwt)
        .contentType(MediaType.APPLICATION_JSON));
    resultActions.andExpect(MockMvcResultMatchers.status().isOk());

    MvcResult mvcResult = resultActions.andReturn();
    String response = mvcResult.getResponse().getContentAsString();
    PasteResponseListResponse pasteResponseWithList = objectMapper.readValue(response, PasteResponseListResponse.class);
    // PasteResponseWithList response = new ObjectMapper()
    //     .registerModule(new JavaTimeModule())
    //     .readValue(responseString, new TypeReference<PasteResponseWithList>() {});
    
    List<Paste> pastes = pasteResponseWithList.getData();
    assertThat(pastes.size()).isEqualTo(3);
  }

  @Test
  void getPaste() throws Exception {
    Paste pasteToSave = new Paste();
    pasteToSave.setContent("demoContent");
    pasteToSave.setName("demoName");
    Paste paste = pasteRepository.save(pasteToSave);
    assertThat(pasteRepository.findAll().size()).isEqualTo(1);

    ResultActions resultActions = mockMvc
        .perform(
          get("/api/v1/pastes/" + paste.getId())
          .header("Authorization", "Bearer " + this.jwt)
          .contentType(MediaType.APPLICATION_JSON));
    resultActions.andExpect(MockMvcResultMatchers.status().isOk());

    MvcResult mvcResult = resultActions.andReturn();
    String response = mvcResult.getResponse().getContentAsString();
    PasteResponse pasteResponse = objectMapper.readValue(response, PasteResponse.class);

    assertThat(pasteResponse.getData())
      .isEqualTo(paste);
  }

  @Test
  void getPasteIsProtected() throws Exception {
    ResultActions resultActions = mockMvc
        .perform(
          get("/api/v1/pastes/" + 1)
          .contentType(MediaType.APPLICATION_JSON));
    resultActions.andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  @Test
  void updatePaste() throws Exception {
    Optional<User> user = userRepository.findByEmail("user@test.com");
    assertThat(user.isPresent()).isTrue();

    Paste pasteToSave = new Paste();
    pasteToSave.setContent("demoContent");
    pasteToSave.setName("demoName");
    pasteToSave.setUser(user.get());
    Paste savedPaste = pasteRepository.save(pasteToSave);

    Paste update = new Paste();
    update.setContent("differentContent");
    update.setName("differentName");

    ResultActions resultActions = mockMvc
        .perform(patch("/api/v1/pastes/" + savedPaste.getId())
        .header("Authorization", "Bearer " + this.jwt)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(update)));

    resultActions.andExpect(MockMvcResultMatchers.status().isOk());

    MvcResult mvcResult = resultActions.andReturn();
    String responseString = mvcResult.getResponse().getContentAsString();
    PasteResponse response = objectMapper
        .readValue(responseString, new TypeReference<PasteResponse>() {});
    Paste pasteResponse = response.getData();

    assertThat(pasteResponse.getContent()).isEqualTo("differentContent");
    assertThat(pasteResponse.getName()).isEqualTo("differentName");
    assertThat(pasteResponse).usingRecursiveComparison().ignoringFields("id", "apiKey").isNotEqualTo(savedPaste);
  }

  @Test
  void updatePasteIsProtected() throws Exception {
    Paste paste = new Paste();

    ResultActions resultActions = mockMvc
        .perform(patch("/api/v1/pastes/" + 1)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(paste)));

    resultActions.andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }
  
  @Test
  void updatePasteReturns404IfNotExists() throws Exception {
    pasteRepository.deleteAll();
    assertThat(pasteRepository.findAll().size()).isEqualTo(0);

    Paste update = new Paste();
    update.setContent("differentContent");
    update.setName("differentName");

    ResultActions resultActions = mockMvc
        .perform(patch("/api/v1/pastes/" + 10)
        .header("Authorization", "Bearer " + this.jwt)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(update)));

    resultActions.andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  void updatePasteReturns400IfNotOwnerOfPaste() throws Exception {
    User userObject = new User("user2@test.com", "password", "firstName", "lastName", "READ, ROLE_USER");
    User user = userRepository.save(userObject);
    userRepository.flush();

    Paste pasteToSave = new Paste();
    pasteToSave.setContent("demoContent");
    pasteToSave.setName("demoName");
    pasteToSave.setUser(user);
    Paste savedPaste = pasteRepository.save(pasteToSave);

    Paste pasteUpdate = new Paste();
    pasteUpdate.setContent("differentContent");
    pasteUpdate.setName("differentName");

    ResultActions resultActions = mockMvc
        .perform(patch("/api/v1/pastes/" + savedPaste.getId())
        .header("Authorization", "Bearer " + this.jwt)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(pasteUpdate)));

    resultActions.andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  @Test
  void updatePasteReturns403IfNotValid() throws Exception {
    Paste pasteToSave = new Paste();
    pasteToSave.setContent("demoContent");
    pasteToSave.setName("demoName");
    Paste savedPaste = pasteRepository.save(pasteToSave);

    Paste update = new Paste();

    ResultActions resultActions = mockMvc
        .perform(patch("/api/v1/pastes/" + savedPaste.getId())
        .header("Authorization", "Bearer " + this.jwt)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(update)));

    resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void deletePaste() throws Exception {
    Paste pasteObject = new Paste();
    pasteObject.setContent("demoContent");
    pasteObject.setName("demoName");
    pasteObject.setUser(this.user);
    Paste paste = pasteRepository.save(pasteObject);

    ResultActions resultActions = mockMvc
        .perform(delete("/api/v1/pastes/{id}", paste.getId())
        .header("Authorization", "Bearer " + this.jwt));
        
    resultActions.andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  void deletePasteReturns404IfNotFound() throws Exception {
    pasteRepository.deleteAll();

    ResultActions resultActions = mockMvc
        .perform(delete("/api/v1/pastes/1")
        .header("Authorization", "Bearer " + this.jwt));
        
    resultActions.andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  void deletePasteIsProtected() throws Exception {
    ResultActions resultActions = mockMvc
        .perform(delete("/api/v1/pastes/1"));
        
    resultActions.andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  @Test
  void deletePasteReturns400IfNotOwnerOfPaste() throws Exception {
    String email = "wronguser@test.org";
    String password = "password";
    User user = new User(email, password, "firstName", "lastName", "READ, ROLE_USER");
    userRepository.save(user);
    userRepository.flush();

    Paste pasteObject = new Paste();
    pasteObject.setContent("demoContent");
    pasteObject.setName("demoName");
    pasteObject.setUser(user);
    Paste paste = pasteRepository.save(pasteObject);

    Map<String, String> loginRequest = new HashMap<>();
    loginRequest.put("email", email);
    loginRequest.put("password", password);

    mockMvc.perform(delete("/api/v1/pastes/" + paste.getId())
      .header("Authorization", "Bearer " + this.jwt)
      .content(objectMapper.writeValueAsString(loginRequest)))
      .andExpect(MockMvcResultMatchers.status().isUnauthorized());

    Optional<Paste> savedPaste = pasteRepository.findById(paste.getId());
    assertThat(savedPaste.isPresent()).isTrue();
  }
  
  private void addPastes() {
    List<Paste> pasteList = new ArrayList<>();

    Paste paste1 = new Paste();
    paste1.setContent("demoContent");
    paste1.setName("demoName");
    pasteList.add(paste1);
    Paste paste2 = new Paste();
    paste2.setContent("demoContent");
    paste2.setName("demoName");
    pasteList.add(paste2);
    Paste paste3 = new Paste();
    paste3.setContent("demoContent");
    paste3.setName("demoName");
    pasteList.add(paste3);
    Paste paste4 = new Paste();
    paste4.setContent("demoContent");
    paste4.setName("demoName");
    pasteList.add(paste4);

    pasteRepository.saveAll(pasteList);
  }
}
