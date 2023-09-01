package com.akinwalehabib.pastebin.controllers.response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
  
  private String message;
  private List<String> errors;

  public ErrorResponse(String message, List<String> errors) {
      super();
      this.message = message;
      this.errors = errors;
  }

  public ErrorResponse(String message, String error) {
      super();
      this.message = message;
      errors = Arrays.asList(error);
  }

  public ErrorResponse(String message) {
      super();
      this.message = message;
      errors = new ArrayList<>();
  }
}
