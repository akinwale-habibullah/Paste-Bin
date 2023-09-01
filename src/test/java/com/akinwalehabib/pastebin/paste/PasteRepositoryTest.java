package com.akinwalehabib.pastebin.paste;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.akinwalehabib.pastebin.data.Gender;
import com.akinwalehabib.pastebin.data.Paste;
import com.akinwalehabib.pastebin.data.PasteRepository;
import com.akinwalehabib.pastebin.data.User;
import com.akinwalehabib.pastebin.data.UserRepository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
public class PasteRepositoryTest {
  
  @Autowired private UserRepository userRepository;
  @Autowired private PasteRepository pasteRepository;

  @AfterEach
  void tearDown() {
    pasteRepository.deleteAll();
  }

  @Test
  void findTop8ByOrderByCreatedOnDescReturns8Pastes() {
    List<Paste> pasteList = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      Paste newPaste = new Paste();
      newPaste.setName("Demo Paste");
      newPaste.setContent("Demo content");
      pasteList.add(newPaste);
    }

    for (Paste paste : pasteList) {
      pasteRepository.save(paste);
    }

    List<Paste> pastes = pasteRepository.findTop8ByOrderByCreatedOnDesc();

    assertThat(pastes.size()).isEqualTo(8);
  }

  @Test
  void itShouldReturnUserById() {
    // create user
    LocalDate dob = LocalDate.of(2023, 1, 1);
    User userToCreate = new User();
    userToCreate.setEmail("demoser@demo.com");
    userToCreate.setPassword("password");
    userToCreate.setFirstName("firstName");
    userToCreate.setMiddleName("middleName");
    userToCreate.setLastName("LastName");
    userToCreate.setAddress("demo address, state");
    userToCreate.setGender(Gender.MALE);
    userToCreate.setDateOfBirth(dob);    
    User user = userRepository.save(userToCreate);

    Paste newPaste = new Paste();
    newPaste.setName("Demo Paste");
    newPaste.setContent("Demo content");
    newPaste.setUser(user);
    pasteRepository.save(newPaste);

    List<Paste> pastes = pasteRepository.findByUser(user);
    assertThat(pastes.size()).isEqualTo(1);
    assertThat(pastes.get(0).getUser()).isEqualTo(user);
  }
}
