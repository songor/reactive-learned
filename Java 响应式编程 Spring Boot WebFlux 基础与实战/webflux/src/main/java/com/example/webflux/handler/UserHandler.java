package com.example.webflux.handler;

import com.example.webflux.domain.User;
import com.example.webflux.repository.UserRepository;
import com.example.webflux.util.UserValidationUtil;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class UserHandler {

  private final UserRepository userRepository;

  public UserHandler(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public Mono<ServerResponse> get(ServerRequest request) {
    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
        .body(this.userRepository.findAll(), User.class);
  }

  public Mono<ServerResponse> create(ServerRequest request) {
    Mono<User> user = request.bodyToMono(User.class);
    return user.flatMap(usr -> {
      UserValidationUtil.check(usr);
      return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
          .body(this.userRepository.save(usr), User.class);
    });
  }

  public Mono<ServerResponse> delete(ServerRequest request) {
    String id = request.pathVariable("id");
    return this.userRepository.findById(id)
        .flatMap(user -> this.userRepository.delete(user).then(ServerResponse.ok().build()))
        .switchIfEmpty(ServerResponse.notFound().build());
  }
}
