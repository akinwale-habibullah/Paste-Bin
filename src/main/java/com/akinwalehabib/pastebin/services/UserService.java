package com.akinwalehabib.pastebin.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.akinwalehabib.pastebin.data.User;
import com.akinwalehabib.pastebin.data.UserRepository;
import com.akinwalehabib.pastebin.security.SecurityUser;

@Service
public class UserService implements UserDetailsService {
  
  private UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return userRepository
      .findByEmail(email)
      .map(SecurityUser::new)
      .orElseThrow(() -> new UsernameNotFoundException("Email not found: " + email));
  }

  public User addUser(User user) {
    return userRepository.save(user);
  }

  public Page<User> getUsersByPage(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    return userRepository.findAll(pageable);
  }

  public Optional<User> findByEmail(String email) throws UsernameNotFoundException {
    return userRepository.findByEmail(email);
  }
}
