package com.akinwalehabib.pastebin.user;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.akinwalehabib.pastebin.data.User;
import com.akinwalehabib.pastebin.data.UserRepository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
public class UserRepositoryTest {

  @Autowired private UserRepository userRepository;

  @BeforeEach
  void beforeEach() {
    User user1 = new User("test1@demo.com", "password", "firstName", "lastName", "READ, ROLE USER");
    User user2 = new User("test2@demo.com", "password", "firstName", "lastName", "READ, ROLE USER");
    User user3 = new User("test3@demo.com", "password", "firstName", "lastName", "READ, ROLE USER");
    User user4 = new User("test4@demo.com", "password", "firstName", "lastName", "READ, ROLE USER");
    User user5 = new User("test5@demo.com", "password", "firstName", "lastName", "READ, ROLE USER");
    List<User> usersList = new ArrayList<>(List.of(user1, user2, user3, user4, user5));
    this.userRepository.saveAll(usersList);
  }

  @AfterEach
  void tearDown() {
    userRepository.deleteAll();
  }

  @Test
  void findByEmail() {
    Optional<User> user = userRepository.findByEmail("test1@demo.com");
    assertThat(user.isPresent()).isTrue();
  }

  @Test
  void findByEmailReturnsNullWhenNonExistent() {
    Optional<User> user = userRepository.findByEmail("nonexistent@notexist.com");
    assertThat(user.isPresent()).isFalse();
  }

  @Test
  void findAllUsingPage() {
    Pageable pages = PageRequest.of(0, 4);
    Page<User> users = userRepository.findAll(pages);
    assertThat(users.toList().size()).isEqualTo(4);

    Pageable pages2 = PageRequest.of(1, 4);
    Page<User> users2 = userRepository.findAll(pages2);
    assertThat(users2.toList().size()).isEqualTo(1);
  }
  
}
