package com.akinwalehabib.pastebin.controllers.exceptions;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
  private List<String> errors;

  public BadRequestException(String msg) {
    super(msg);
    this.errors = new ArrayList<>();
  }

  public BadRequestException(String msg, List<String> errors) {
    super(msg);
    this.errors = errors;
  }

  public List<String> getErrors() {
    return this.errors;
  }
}
