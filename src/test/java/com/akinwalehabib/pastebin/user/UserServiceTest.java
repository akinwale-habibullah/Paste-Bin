package com.akinwalehabib.pastebin.user;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import com.akinwalehabib.pastebin.data.User;
import com.akinwalehabib.pastebin.data.UserRepository;
import com.akinwalehabib.pastebin.services.UserService;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
  
  @Mock
  private UserRepository userRepository;
  @Autowired
  private UserService underTest;

  @BeforeEach
  void setUp() {
    underTest = new UserService(userRepository);
  }

  @Test
  void addUser() {
    User user = new User("testemail@test.com", "password", "firstName", "lastName", "READ, ROLE_USER");
    
    underTest.addUser(user);

    ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
    verify(userRepository).save(userArgumentCaptor.capture());
    assertThat(userArgumentCaptor.getValue()).isEqualTo(user);
  }

  @Test
  void getUsersByPage() {
    underTest.getUsersByPage(0, 4);

    ArgumentCaptor<PageRequest> pageRequestArgumentCaptor = ArgumentCaptor.forClass(PageRequest.class);
    verify(userRepository).findAll(pageRequestArgumentCaptor.capture());
    assertThat(pageRequestArgumentCaptor.getValue()).isEqualTo(PageRequest.of(0, 4));
  }

  @Test
  void findByEmail() {
    String email = "user@test.com";
    underTest.findByEmail(email);

    ArgumentCaptor<String> emailArgumentCaptor = ArgumentCaptor.forClass(String.class);
    verify(userRepository).findByEmail(emailArgumentCaptor.capture());
    assertThat(emailArgumentCaptor.getValue()).isEqualTo(email);
  }

  @Test
  void loadUserByUsername() {
    String email = "testemail@test.com";
    User userObject = new User(email, "password", "firstName", "lastName", "READ, ROLE_USER");
    Optional<User> user = Optional.of(userObject);
    when(userRepository.findByEmail(email)).thenReturn(user);
    
    underTest.loadUserByUsername(email);

    ArgumentCaptor<String> emailArgumentCaptor = ArgumentCaptor.forClass(String.class);
    verify(userRepository).findByEmail(emailArgumentCaptor.capture());
    assertThat(emailArgumentCaptor.getValue()).isEqualTo(email);
  }
}
