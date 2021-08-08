package com.example.webflux.client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
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
//    user.subscribe(usr -> System.out.println("get a user: " + usr));

//    userAPIServer.deleteById("610fb9faeb831b72b39f69df")
//        .subscribe(usr -> System.out.println("delete done"));
  }
}
