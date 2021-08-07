package com.example.webflux.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

@ControllerAdvice
public class ParamValidationAdvice {

  @ExceptionHandler(WebExchangeBindException.class)
  public ResponseEntity<String> handleBindException(WebExchangeBindException ex) {
    return new ResponseEntity<>(toStr(ex), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  private String toStr(WebExchangeBindException ex) {
    return ex.getFieldErrors().stream()
        .map(e -> e.getField() + ": " + e.getDefaultMessage())
        .reduce("", (s1, s2) -> s1 + " | " + s2);
  }
}
