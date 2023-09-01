package com.akinwalehabib.pastebin.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.akinwalehabib.pastebin.data.Paste;
import com.akinwalehabib.pastebin.data.PasteRepository;

@Service
public class PasteService {
  @Autowired
  PasteRepository pasteRepository;

  public PasteService(PasteRepository pasteRepository) {
    this.pasteRepository = pasteRepository;
  }

  public Optional<Paste> getPaste(long id) {
    return pasteRepository.findById(id);
  }

  public List<Paste> getAllPastes() {
    return pasteRepository.findAll();
  }

  public List<Paste> getTopPastes() {
    return pasteRepository.findTop8ByOrderByCreatedOnDesc();
  }

  public Page<Paste> getAllPastesByPage(int pageNumber, int pageSize) {
    Pageable pageable = PageRequest.of(pageNumber, pageSize);
    return pasteRepository.findAll(pageable);
  }

  public Paste createPaste(Paste paste) {
    return pasteRepository.save(paste);
  }

  public Paste updatePaste(long id, Paste update, Paste paste) {
    if (update.getName() != null) {
      paste.setName(update.getName());
    }

    if (update.getContent() != null) {
      paste.setContent(update.getContent());
    }

    return pasteRepository.save(paste);
  }

  public void deletePaste(long id) {
    pasteRepository.deleteById(id);
  }
}
