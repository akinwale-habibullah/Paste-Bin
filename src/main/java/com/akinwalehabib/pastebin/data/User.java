package com.akinwalehabib.pastebin.data;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
 
@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User  {

  public User(String email, String password, String firstName, String lastName, String roles) {
    this.password = password;
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.roles = roles;
  }

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID apiKey;

  @NotNull(message = "Email must not be null")
  @NotBlank(message = "Email must not be blank")
  @Email
  @Column(nullable = false, unique = true)
  private String email;

  @NotNull(message = "Password must not be null")
  @NotBlank(message = "Password must not be blank")
  @Size(min = 8, max = 100, message = "Password size must be between 8 and 100 characters")
  private String password;

  @NotNull
  @NotBlank
  @Size(min = 3, max = 100, message = "Firstname size must be between 3 and 100 characters")
  private String firstName;
  
  private String middleName;

  @NotNull
  @NotBlank
  @Size(min = 3, max = 100, message = "Lastname size must be between 3 and 100 characters")
  private String lastName;
  
  @Enumerated(EnumType.STRING)
  private Gender gender = Gender.UNKNOWN;
  private LocalDate dateOfBirth;
  private String address;
  private String roles = "READ, ROLE_USER";
  
  @Transient
  @OneToMany(mappedBy = "user")
  private Set<Paste> pastes;

  @PrePersist
  public void generateApiKey() {
    if (apiKey == null) {
      apiKey = UUID.randomUUID();
    }

    if (this.id == null) {
      this.password = new BCryptPasswordEncoder().encode(this.password);
    }
  }
}
