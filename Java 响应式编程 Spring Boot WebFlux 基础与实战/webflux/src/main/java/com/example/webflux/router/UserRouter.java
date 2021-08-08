package com.example.webflux.router;

import com.example.webflux.handler.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class UserRouter {

  @Bean
  RouterFunction<ServerResponse> router(UserHandler handler) {
    return RouterFunctions.nest(RequestPredicates.path("/router/user"),
        RouterFunctions.route(RequestPredicates.GET("/"), handler::get)
            .andRoute(RequestPredicates.POST("/")
                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::create)
            .andRoute(RequestPredicates.DELETE("/{id}"), handler::delete));
  }
}
