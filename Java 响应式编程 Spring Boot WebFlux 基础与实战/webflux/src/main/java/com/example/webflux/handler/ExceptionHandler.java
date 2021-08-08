package com.example.webflux.handler;

import java.nio.charset.StandardCharsets;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

@Component
@Order(-5)
public class ExceptionHandler implements WebExceptionHandler {

  @Override
  public Mono<Void> handle(ServerWebExchange serverWebExchange, Throwable throwable) {
    ServerHttpResponse response = serverWebExchange.getResponse();
    response.setStatusCode(HttpStatus.BAD_REQUEST);
    response.getHeaders().setContentType(MediaType.TEXT_PLAIN);
    DataBuffer buffer = response.bufferFactory()
        .wrap(throwable.toString().getBytes(StandardCharsets.UTF_8));
    return response.writeWith(Mono.just(buffer));
  }
}
