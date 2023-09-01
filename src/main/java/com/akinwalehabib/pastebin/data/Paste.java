package com.akinwalehabib.pastebin.data;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Paste {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull(message = "Content must not be null")
  @NotBlank(message = "Content must not be blank")
  private String content;

  @NotNull(message = "Name must not be null")
  @NotBlank(message = "Name must not be blank")
  private String name;
  private String password;
  
  @ManyToOne
  @JoinColumn(name = "user.id")
  private User user;

  private List<String> tags;
  private String folder;
  private boolean syntaxHighlight;
  private String syntax;

  @Enumerated(EnumType.STRING)
  private Category category;

  private LocalDateTime createdOn = LocalDateTime.now();
  private LocalDateTime updatedOn;
  private String expiresOn; // TODO: Use LocalDate type
  private boolean burnAfterRead;
  private String fileSize;

  @Enumerated(EnumType.STRING)
  private Exposure exposure = Exposure.PUBLIC;
}
