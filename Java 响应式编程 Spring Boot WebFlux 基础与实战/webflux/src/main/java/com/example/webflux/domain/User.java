package com.example.webflux.domain;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user")
@Data
public class User {

  @Id
  private String id;

  @NotBlank
  private String name;

  @Range(min = 18L, max = 100L)
  private Integer age;
}
