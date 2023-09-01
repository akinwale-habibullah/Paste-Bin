package com.akinwalehabib.pastebin.paste;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.akinwalehabib.pastebin.data.Paste;
import com.akinwalehabib.pastebin.data.PasteRepository;
import com.akinwalehabib.pastebin.services.PasteService;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class PasteServiceTest {
  
  @Mock
  @Autowired
  private PasteRepository pasteRepository;
  @Autowired
  private PasteService underTest;

  @BeforeEach
  void setUp() {
    underTest = new PasteService(pasteRepository);
  }

  @Test
  void getAllPastes() {
    underTest.getAllPastes();
    verify(pasteRepository).findAll();
  }

  @Test
  void getOnePaste() {
    long id = 1;
    underTest.getPaste(id);
    verify(pasteRepository).findById(id);
  }

  @Test
  void getTopPastes() {
    underTest.getTopPastes();
    verify(pasteRepository).findTop8ByOrderByCreatedOnDesc();
  }

  @Test
  void getAllPastesByPage() {
    underTest.getAllPastesByPage(1, 2);

    ArgumentCaptor<Pageable> pageRequestCaptor = ArgumentCaptor.forClass(Pageable.class);
    verify(pasteRepository).findAll(pageRequestCaptor.capture());
    assertThat(pageRequestCaptor.getValue()).isEqualTo(PageRequest.of(1, 2));
  }

  @Test
  void createPaste() {
    Paste paste = new Paste();
    paste.setContent("content\n");
    paste.setName("paste");

    underTest.createPaste(paste);
    
    ArgumentCaptor<Paste> pasteArgumentCaptor = ArgumentCaptor.forClass(Paste.class);
    verify(pasteRepository).save(pasteArgumentCaptor.capture());
    assertThat(pasteArgumentCaptor.getValue()).isEqualTo(paste);
  }

  @Test
  void updatePaste() {
    Paste paste = new Paste();
    long id = 1;
    paste.setId(id);
    paste.setContent("content");
    paste.setName("name");

    Paste update = new Paste();
    update.setContent("differentContent");
    update.setName("differentName");

    underTest.updatePaste(id, update, paste);
    ArgumentCaptor<Paste> pasteArgumentCaptor = ArgumentCaptor.forClass(Paste.class);

    verify(pasteRepository).save(pasteArgumentCaptor.capture());
    assertThat(pasteArgumentCaptor.getValue().getContent()).isEqualTo(update.getContent());
    assertThat(pasteArgumentCaptor.getValue().getName()).isEqualTo(update.getName());
  }

  @Test
  void deletePaste() {
    long id = 1;
    underTest.deletePaste((int) id);

    ArgumentCaptor<Long> idArgumentCaptor = ArgumentCaptor.forClass(Long.class);
    verify(pasteRepository).deleteById(idArgumentCaptor.capture());
    assertThat(idArgumentCaptor.getValue()).isEqualTo(id);
  }
}
