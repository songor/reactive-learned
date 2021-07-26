package com.example.spring.boot.webflux;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class SpringBootWebfluxApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringBootWebfluxApplication.class, args);
  }

  @Bean
  public RouterFunction<ServerResponse> routerFunction() {
//    return RouterFunctions.route(serverRequest -> "/hello".equals(serverRequest.uri().getPath()),
//        serverRequest -> ServerResponse.status(
//            HttpStatus.OK).body(Mono.just("hello world"), String.class));

    return route(GET("/hello"),
        serverRequest -> {
          System.out.println("[" + Thread.currentThread().getName() + "] /hello");
          return ServerResponse.status(HttpStatus.OK)
              .body(Mono.just("hello world"), String.class);
        });
  }
}
