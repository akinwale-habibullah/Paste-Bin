package com.akinwalehabib.pastebin.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.akinwalehabib.pastebin.controllers.exceptions.BadRequestException;
import com.akinwalehabib.pastebin.controllers.exceptions.ResourceNotFoundException;
import com.akinwalehabib.pastebin.controllers.exceptions.UnauthorizedException;
import com.akinwalehabib.pastebin.controllers.response.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllersExceptionHandler extends ResponseEntityExceptionHandler {
  
  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorResponse> handleBadRequestException(HttpServletRequest request, BadRequestException ex) {

    ErrorResponse err = new ErrorResponse(
        ex.getMessage(),
        ex.getErrors());
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(err);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  ResponseEntity<ErrorResponse> handleNoHandlerFoundException(HttpServletRequest request, ResourceNotFoundException ex) {
    ErrorResponse err = new ErrorResponse(
        ex.getMessage(),
        "Resource does not exist");

    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(err);
  }

  @ExceptionHandler(UnauthorizedException.class)
  ResponseEntity<ErrorResponse> handleUnAuthorizedException(HttpServletRequest request, UnauthorizedException ex) {
    ErrorResponse err = new ErrorResponse(ex.getMessage());

    return ResponseEntity
        .status(HttpStatus.UNAUTHORIZED)
        .body(err);
  }
}
