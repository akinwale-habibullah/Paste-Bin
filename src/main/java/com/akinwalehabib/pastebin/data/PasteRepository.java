package com.akinwalehabib.pastebin.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PasteRepository extends JpaRepository<Paste, Long> {

  List<Paste> findTop8ByOrderByCreatedOnDesc();
  List<Paste> findByUser(User user);
}
