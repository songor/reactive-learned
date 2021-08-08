package com.example.webflux.client;

import com.example.framework.ApiServer;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ApiServer("http://localhost:8080/user")
public interface IUserAPIServer {

  @GetMapping("/")
  Flux<User> getAll();

  @GetMapping("/{id}")
  Mono<User> getById(@PathVariable("id") String id);

  @PostMapping("/")
  Mono<User> create(@RequestBody Mono<User> user);

  @DeleteMapping("/{id}")
  Mono<Void> deleteById(@PathVariable("id") String id);
}
