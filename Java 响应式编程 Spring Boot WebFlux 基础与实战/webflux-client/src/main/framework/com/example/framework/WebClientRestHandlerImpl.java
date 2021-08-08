package com.example.framework;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import reactor.core.publisher.Mono;

public class WebClientRestHandlerImpl implements RestHandler {

  private WebClient webClient;

  @Override
  public void init(ServerInfo serverInfo) {
    this.webClient = WebClient.create(serverInfo.getUrl());
  }

  @Override
  public Object invoke(MethodInfo methodInfo) {
    RequestBodySpec requestBodySpec = this.webClient.method(methodInfo.getMethod())
        .uri(methodInfo.getUri(), methodInfo.getParams())
        .accept(MediaType.APPLICATION_JSON);

    ResponseSpec responseSpec;
    if (methodInfo.getBody() != null) {
      responseSpec = requestBodySpec.body(methodInfo.getBody(), methodInfo.getBodyElementType())
          .retrieve();
    } else {
      responseSpec = requestBodySpec.retrieve();
    }

    responseSpec.onStatus(HttpStatus::is4xxClientError,
        clientResponse -> Mono.error(new RuntimeException(clientResponse.statusCode().name())));

    Object result;
    if (methodInfo.getReturnFlux()) {
      result = responseSpec.bodyToFlux(methodInfo.getReturnElementType());
    } else {
      result = responseSpec.bodyToMono(methodInfo.getReturnElementType());
    }
    return result;
  }
}
