package com.akinwalehabib.pastebin.controllers.response;

import com.akinwalehabib.pastebin.data.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginResponse{
  
  private User user;
  private String token;

  public LoginResponse(User user, String token) {
      super();
      this.user = user;
      this.token = token;
  }
}
