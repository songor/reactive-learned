package com.example.framework;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

@Getter
@Setter
public class MethodInfo {

  private String uri;

  private HttpMethod method;

  private Map<String, Object> params;

  private Mono<?> body;

  private Class<?> bodyElementType;

  private Boolean returnFlux;

  private Class<?> returnElementType;
}
