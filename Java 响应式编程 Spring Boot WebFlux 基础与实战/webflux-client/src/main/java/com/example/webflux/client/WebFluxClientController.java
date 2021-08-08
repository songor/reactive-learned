package com.example.webflux.client;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class WebFluxClientController {

  private final IUserAPIServer userAPIServer;

  public WebFluxClientController(IUserAPIServer userAPIServer) {
    this.userAPIServer = userAPIServer;
  }

  @GetMapping("/")
  public void get() {
//    User aUser = new User();
//    aUser.setName("z");
//    aUser.setAge(40);
//    Mono<User> result = userAPIServer.create(Mono.just(aUser));
//    result.subscribe(usr -> System.out.println("create a user: " + usr));

    Flux<User> users = userAPIServer.getAll();
    users.subscribe(usr -> System.out.println("get all users: " + usr));

//    Mono<User> user = userAPIServer.getById("610e015a36fcbc66c0747722");
    Mono<User> user = userAPIServer.getById("610fb9faeb831b72b39f69df");
    user.subscribe(usr -> System.out.println("get a user: " + usr),
        error -> System.out.println("can't get user: " + error.getMessage()));

//    userAPIServer.deleteById("610fb9faeb831b72b39f69df")
//        .subscribe(usr -> System.out.println("delete done"));

    Mono<User> result = WebClient.create("http://localhost:8080/user").get()
        .uri("/{id}", "610fb9faeb831b72b39f69df").accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .onStatus(HttpStatus::is4xxClientError,
            response -> Mono.error(new RuntimeException("4xx error")))
        .bodyToMono(User.class);
    result.subscribe(usr -> System.out.println("get a user: " + usr),
        error -> System.out.println("can't get user: " + error.getMessage()));
  }
}
