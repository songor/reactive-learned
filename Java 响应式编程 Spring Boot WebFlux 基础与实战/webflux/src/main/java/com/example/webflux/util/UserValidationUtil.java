package com.example.webflux.util;

import com.example.webflux.domain.User;
import java.util.stream.Stream;

public class UserValidationUtil {

  private static final String[] INVALID_NAME = {"admin"};

  public static void check(User user) {
    Stream.of(INVALID_NAME).filter(invalidName -> invalidName.equalsIgnoreCase(user.getName()))
        .findAny().ifPresent(invalidName -> {
      throw new IllegalArgumentException("invalid request param: " + invalidName);
    });
  }
}
